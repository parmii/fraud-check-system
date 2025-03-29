package com.example.paymentprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.example.paymentsystem"})
public class PaymentprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentprocessorApplication.class, args);
	}

}
