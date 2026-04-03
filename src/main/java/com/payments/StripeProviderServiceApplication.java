package com.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class StripeProviderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StripeProviderServiceApplication.class, args);
		log.info("Stripe-Provider-Service started successfully..........");
	}

}
