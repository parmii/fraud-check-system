package com.example.fraudcheck.config;

import com.example.fraudcheck.service.FraudCheckService;
import com.example.paymentsystem.service.AuditLogService;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.fraudcheck.config.FraudCheckRoute;

@Configuration
public class CamelConfig {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private FraudCheckService fraudCheckService;

    @Bean
    public CamelContext camelContext() throws Exception{
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addRoutes(new FraudCheckRoute(auditLogService,fraudCheckService));
        camelContext.start();
        return camelContext; // Explicitly creating the CamelContext bean
    }

    @Bean
    public ProducerTemplate producerTemplate(CamelContext camelContext) {
        return camelContext.createProducerTemplate();
    }
}
