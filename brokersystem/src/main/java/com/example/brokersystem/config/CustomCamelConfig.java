package com.example.brokersystem.config;

import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CamelAutoConfiguration.class)
public class CustomCamelConfig {
}
