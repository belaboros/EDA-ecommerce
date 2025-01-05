package org.msffp.order.listener;


import java.util.List;
import java.util.logging.Level;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.msffp.order.model.Product;
import org.msffp.order.model.ProductParsingException;
import org.msffp.order.service.ProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.stereotype.Component;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


/// Listen to Product:Changed domain events from a Kafka topic
/// and maintain an up-to-date local replica in RDBMS repository.
/// 
/// See also:
/// * https://docs.spring.io/spring-kafka/reference/quick-tour.html  
/// * https://docs.spring.io/spring-kafka/reference/kafka/annotation-error-handling.html
/// * https://developer.confluent.io/get-started/spring-boot/ 
/// * https://docs.spring.io/spring-kafka/api/index.html
@Component
@RequiredArgsConstructor
@Log
public class ProductEventConsumer {

    private final ProductService productService;

    /// BatchAcknowledgingMessageListener to process domain event messages with higher throughput.
    /// See: 
    /// https://docs.spring.io/spring-kafka/reference/kafka/receiving-messages/message-listeners.html
    @KafkaListener(topics = "MSfFP.ProductMgmt.event.Product", groupId = "MSfFP.OrderMgmt3")
    private void listen(List<ConsumerRecord<Object, Object>> records) {
        log.info("Received " + records.size() + " Product:* domain event messages.\n START processing.");

        int index = -1;
        for (ConsumerRecord<Object, Object> cr : records) {
            index++;
            String productEvent = (String)cr.value();
            log.fine("Received and processing product domain event message: \n" + productEvent);
            try {
                Product p = Product.fromJson(productEvent);
                Product persisted = productService.upsert(p);
                log.info("Received and updated product: " + persisted);
            } catch (ProductParsingException e) {
                throw new BatchListenerFailedException("Received product domain event message with invalid JSON content.", e, index);
            } catch (OptimisticLockException e) {
                // Ignore silently identical and out-of-order/late product messages.
                // Order service already has a local product record which is identical or has a higher version attribute.
                // Just log a warning.
                log.log(Level.WARNING, "Ignored (out-of-order/late or duplicate) product message.", e);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // cancel processing the batch of ProductEvents
                // consumer offset will be committed till the last sucessfully processed Product message
                throw new BatchListenerFailedException("Received invalid product domain event message which violates DB contraint(s).", e, index);
            } catch (Exception e) {
                throw new BatchListenerFailedException("Failed to process/store product domain event message", e, index);
            }
        }
    }
}
