package org.msffp.product;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msffp.product.model.Product;
import org.msffp.product.service.ProductService;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class ProductApp {

	public static void main(String[] args) {
		SpringApplication.run(ProductApp.class, args);
	}

	@Autowired
	ProductService productService;

	/**
	 * Generate random Orders if there is none.
	 * This is executed if app.db.init.enabled = true
	 * @return
	 */
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "db.init.enabled", havingValue = "true")
    public CommandLineRunner generateRandomOrders() {
        return _ -> {
			if (!productService.findAll().isEmpty()) {
				return;
			}
			List<Product> randomOrders = productService.generateRandom(30);

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

			productService.saveAll(randomOrders);
        };
	}
}
