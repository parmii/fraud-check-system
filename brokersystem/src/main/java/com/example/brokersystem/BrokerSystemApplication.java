package com.example.brokersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.brokersystem.controller", "com.example.brokersystem.config"," com.example.paymentsystem"})
public class BrokerSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokerSystemApplication.class, args);
	}

}
