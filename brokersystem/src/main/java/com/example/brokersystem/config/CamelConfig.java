package com.example.brokersystem.config;

import com.example.paymentsystem.service.AuditLogService;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Autowired
    private AuditLogService auditLogService;

    @Bean
    public CamelContext camelContext() throws Exception{
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new BrokerSystemRoute(auditLogService));
        context.setTracing(true);
        context.start();
        return context; // Explicitly creating the CamelContext bean
    }

    @Bean
    public ProducerTemplate producerTemplate(CamelContext camelContext) {
        return camelContext.createProducerTemplate();
    }
}
