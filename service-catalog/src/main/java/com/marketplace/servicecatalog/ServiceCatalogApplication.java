package com.marketplace.servicecatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Service Catalog Microservice",version = "1.0",
description = "APIs Documentation for Service Catalog Microservice"))
@SpringBootApplication(scanBasePackages = "com.marketplace.servicecatalog")
public class ServiceCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceCatalogApplication.class, args);
	}

}
