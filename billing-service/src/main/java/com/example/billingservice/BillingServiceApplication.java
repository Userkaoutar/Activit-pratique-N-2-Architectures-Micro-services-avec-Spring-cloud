package com.example.billingservice;

import com.example.billingservice.entities.Bill;
import com.example.billingservice.entities.ProductItem;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductItemRestClient;
import com.example.billingservice.models.Customer;
import com.example.billingservice.models.Product;
import com.example.billingservice.repository.BillRepository;
import com.example.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BillRepository billRepository,
							ProductItemRepository productItemRepository,
							CustomerRestClient customerRestClient,
							ProductItemRestClient productItemRestClient) {
		return args -> {
			Customer customer = customerRestClient.getCustomerById(1L);
			Bill bill = billRepository.save(new Bill(null, new Date(), null, customer.getId(), null));
				PagedModel<Product> productPagedModel = productItemRestClient.pageProduct(0,10);
				productPagedModel.forEach(p -> {
					ProductItem productItem = new ProductItem();
					productItem.setPrice(p.getPrice());
					productItem.setQuantity(1+new Random().nextInt(100));
					productItem.setBill(bill);
					productItem.setProductID(p.getId());
					productItemRepository.save(productItem);
				});




		};
	}
}
