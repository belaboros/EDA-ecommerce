package org.msffp.order.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONObject;
import org.msffp.order.domainevent.DomainEvent;
import org.msffp.order.model.Order;
import org.msffp.order.model.OrderLine;
import org.msffp.order.model.OrderNotFoundException;
import org.msffp.order.model.OrderStatus;
import org.msffp.order.model.Product;
import org.msffp.order.repository.DomainEventRepository;
import org.msffp.order.repository.OrderRepository;
import org.msffp.order.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DomainEventRepository domainEventRepository;

    private final ProductService productService;


    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order submitNewOrder(Order order) {
        if (order.getId() != null) {
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order. Expected null ID. Received: " + order.toString());
            throw new IllegalArgumentException("Invalid order. Null ID is expected for newly submitted orders. Received: " + order.toString());
        }

        validateSubmittedOrder(order);
        return save(order);
    } 

    @Transactional
    public Order modify(Order o) {
        if (o.isNew()) {
            throw new IllegalArgumentException("New (unpersisted) orders cannot be modified: " + o.toString());
        }

        Order original = orderRepository.findById(o.getId()).orElseThrow(() -> new OrderNotFoundException(o.getId()));

        if (original.getStatus() != OrderStatus.CREATED) {
            throw new IllegalArgumentException("Only orders in " + OrderStatus.CREATED + " state can be modififed. Received: " + original);
        }

        // CustomerID must be identical
        if (original.getCustomerID() != o.getCustomerID()) {
            throw new IllegalArgumentException("Expected order with customer ID: " + original.getCustomerID() + ". But received: " + o);
        }

        validateSubmittedOrder(o);
        Order persisted = orderRepository.save(o);
        return persisted;
    }

    @Transactional
    public Order fulfill(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (o.getStatus() == OrderStatus.FULFILLED) {
            return o;
        }

        if (o.getStatus() != OrderStatus.CREATED) {
            throw new IllegalArgumentException("Expected order with " + OrderStatus.CREATED + " status, but got: " + o.toString());
        }

        // ... fulfillment happens here ...
        o.setStatus(OrderStatus.FULFILLED);

        Order orderPersisted = orderRepository.save(o);
        domainEventRepository.save(DomainEvent.create(orderPersisted));
        return orderPersisted;
    }

    @Transactional
    public Order cancel(Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (o.getStatus() == OrderStatus.CANCELLED) {
            return o;
        }

        if (o.getStatus() != OrderStatus.CREATED) {
            throw new IllegalArgumentException("Expected order with " + OrderStatus.CREATED + " status, but got: " + o.toString());
        }

        // ... cancellation happens here ...
        o.setStatus(OrderStatus.CANCELLED);

        Order orderPersisted = orderRepository.save(o);
        domainEventRepository.save(DomainEvent.create(orderPersisted));
        return orderPersisted;
    }

    @Transactional
    public Order save(Order order) {
        Order orderPersisted = orderRepository.save(order);
        domainEventRepository.save(DomainEvent.create(orderPersisted));
        return orderPersisted;
    }

    @Transactional
    public List<Order> saveAll(List<Order> orders) {
        List<Order> ordersPersisted = orderRepository.saveAll(orders);
        domainEventRepository.saveAll(ordersPersisted.stream().map(op -> DomainEvent.create(op)).toList());
        return ordersPersisted;
    }
    
    /// Validate newly submitted orders:
    ///  - contains only active products
    ///  - take current unit prices
    ///  - calculate total price
    ///  - set parent order for each OrderLine item
    ///  - ...
    /// @param submittedOrder
    private void validateSubmittedOrder(Order submittedOrder) {
        boolean newOrder = submittedOrder.isNew();

        // delivery day cannot be in the past
        if (submittedOrder.getDeliveryDay().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Delivery date in the past is not allowed: " + submittedOrder.toString());
        }

        if (submittedOrder.getLineItems().isEmpty()) {
            throw new IllegalArgumentException("Missing order line items: " + submittedOrder.toString());
        }

        double[] totalPrice = new double[] {0};
        submittedOrder.getLineItems().stream().forEach(ol -> {
                boolean newOrderLine = ol.getId() == null;

                //zero or negative quantity cannot be ordered
                if (ol.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Positive quantity is expected, but received: " + ol.toString());
                }

                Product p = productRepository
                .findById(ol.getProductID())
                .orElseThrow(() -> new IllegalArgumentException("Unknown product ID: " + ol.getProductID()));

                // new orderLine item cannot contain inactive products
                if ((newOrder || newOrderLine) && !p.isActive()) {
                    throw new IllegalStateException("New order/order-line with inactive product is not allowed: " + p.toString());
                }

                // refresh unitPriceAtShopping if it is missing
                if (newOrder || newOrderLine) {
                    ol.setUnitPriceAtShopping(p.getUnitPrice());
                }

                // set the order
                ol.setOrder(submittedOrder);

                totalPrice[0] += ol.getQuantity() * ol.getUnitPriceAtShopping();
            }
        );

        submittedOrder.setTotalPrice(totalPrice[0]);

        // set mandatory initial fields on newly created orders
        if (newOrder) {
            submittedOrder.setStatus(OrderStatus.CREATED);
            submittedOrder.setVersion(0);
        }
    }

    @Transactional
    public Order generateRandomOrder() {
        return generateRandomOrders(1).get(0);
    }

    @Transactional
    public List<Order> generateRandomOrders(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Positive count is expected, but received: " + count);
        }

        List<Order> randomOrders = IntStream
            .range(0, count)
            .mapToObj(idx -> createRandomOrder(idx))
            .toList();

        String NL = System.getProperty("line.separator");
        ObjectMapper mapper = new ObjectMapper();
        // support Java 8 date time apis
        mapper.registerModule(new JavaTimeModule());
        // StdDateFormat is ISO8601 since jackson 2.9
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        log.info(
            randomOrders.stream()
                .limit(5)
                //.map(o -> o.toString())
                //.collect(Collectors.joining(NL+"    ", NL+ "Saving generated random orders:"+NL+"    ", ""))
                
                .map(o -> new JSONObject(o).toString(4))
                .collect(Collectors.joining(NL, NL+ "Saving generated random orders:"+NL, ""))
        );

        List<Order> persisted = saveAll(randomOrders);
        return persisted;
    }

    private static RandomGenerator rg = java.util.random.RandomGenerator.getDefault();
    private static LocalDate orderWindowStartDay = LocalDate.now().plusDays(1);
    private static long orderWindowLength = 100;

    private Order createRandomOrder(int idx) {
        OrderStatus status = null;
        if (idx < OrderStatus.values().length) {
            status = OrderStatus.values()[idx];
        } else {
            status = OrderStatus.values()[rg.nextInt(0, OrderStatus.values().length)];
        }

        Order o = Order.builder()
            .customerID(rg.nextInt(1, 100))
            .deliveryDay(orderWindowStartDay.plusDays(rg.nextLong(0L, orderWindowLength)))
            .totalPrice(rg.nextInt(1, 20)*1.0)
            .status(status)
            .build();

        IntStream
            .range(0, rg.nextInt(1, 5))
            .forEach(_ -> o.addOrderLine(createRandomOrderLine()));
        
        validateSubmittedOrder(o);

        o.setStatus(status);

        return o;
    }


    private OrderLine createRandomOrderLine() {
        OrderLine ol = OrderLine.builder()
                .productID(productService.getRandomActiveProduct().getId())
                .quantity(rg.nextInt(1, 10))
                .build();

        return ol;
    }

    @Transactional
    public void deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new OrderNotFoundException(id);
        }
    }
}
