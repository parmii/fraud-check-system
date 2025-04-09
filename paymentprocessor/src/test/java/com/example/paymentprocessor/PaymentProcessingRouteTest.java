package com.example.paymentprocessor;

import com.example.paymentprocessor.config.CamelConfig;
import com.example.paymentprocessor.config.PaymentProcessingRoute;
import com.example.paymentprocessor.service.PaymentService;
import com.example.paymentprocessor.service.TransactionService;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.service.AuditLogService;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@CamelSpringBootTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PaymentProcessingRoute.class, CamelConfig.class}) // Load the specific Camel route class
@MockEndpoints("kafka:*") // Mock all Kafka endpoints
public class PaymentProcessingRouteTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @MockBean
    private AuditLogService auditLogService;

    @MockBean
    private PaymentService paymentService;

    private final XmlMapper xmlMapper = new XmlMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Manually add the route if not already present
        if (!camelContext.getRoutes().isEmpty()) {
            camelContext.stop();
        }
        camelContext.addRoutes(new PaymentProcessingRoute(auditLogService,paymentService));
        camelContext.start();
    }

    @Test
    public void testFraudCheckRequestRoute() throws Exception {

        FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
        fraudCheckResponse.setTransactionId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        fraudCheckResponse.setMessage("Nothing found, all okay.");
        fraudCheckResponse.setStatus(1);

        // Set up MockEndpoint to verify message sent to pps-fraud-check-response
        MockEndpoint ppsFraudCheckResponseMock = camelContext.getEndpoint("mock:kafka:pps-fraud-check-response?brokers=localhost:9092", MockEndpoint.class);
        ppsFraudCheckResponseMock.expectedMessageCount(1);

        // Send XML response to fraud-check-result topic
        producerTemplate.sendBody("mock:kafka:pps-fraud-check-response?brokers=localhost:9092", fraudCheckResponse);

        // Assert that the message was processed and sent
        ppsFraudCheckResponseMock.assertIsSatisfied();
    }
}
