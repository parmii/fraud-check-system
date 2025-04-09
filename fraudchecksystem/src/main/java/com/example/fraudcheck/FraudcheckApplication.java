package com.example.fraudcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.fraudcheck", "com.example.paymentsystem"})
public class FraudcheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudcheckApplication.class, args);
	}

}
