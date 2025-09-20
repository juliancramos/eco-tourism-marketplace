package com.marketplace.servicecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.marketplace.servicecatalog")
public class ServiceCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCatalogApplication.class, args);
	}

}
