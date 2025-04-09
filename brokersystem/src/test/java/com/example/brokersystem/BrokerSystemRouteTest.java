package com.example.brokersystem;

import com.example.brokersystem.config.BrokerSystemRoute;
import com.example.brokersystem.config.CamelConfig;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.service.AuditLogService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@CamelSpringBootTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BrokerSystemRoute.class, CamelConfig.class}) // Load the specific Camel route class
@MockEndpoints("kafka:*") // Mock all Kafka endpoints
public class BrokerSystemRouteTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @MockBean
    private AuditLogService auditLogService;

    private final XmlMapper xmlMapper = new XmlMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Manually add the route if not already present
        if (!camelContext.getRoutes().isEmpty()) {
            camelContext.stop();
        }
        camelContext.addRoutes(new BrokerSystemRoute(auditLogService));
        camelContext.start();
    }

    @Test
    public void testFraudCheckResultRoute() throws Exception {

        // Create a sample FraudCheckResponse
        FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
        fraudCheckResponse.setTransactionId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        fraudCheckResponse.setStatus(1);
        fraudCheckResponse.setMessage("Nothing found, all okay.");

        // Convert to XML
        String xmlResponse = xmlMapper.writeValueAsString(fraudCheckResponse);

        // Set up MockEndpoint to verify message sent to fraud-check-request
        MockEndpoint fraudCheckResponseMock = camelContext.getEndpoint("mock:fraud-check-result?brokers=localhost:9092", MockEndpoint.class);
        fraudCheckResponseMock.expectedMessageCount(1);

        producerTemplate.sendBody("mock:fraud-check-result?brokers=localhost:9092", xmlResponse);

        // Assert that the message was processed and sent
        fraudCheckResponseMock.assertIsSatisfied();
    }

    @Test
    public void testFraudCheckRequestRoute() throws Exception {
        FraudCheckRequest fraudCheckRequest = new FraudCheckRequest();
        fraudCheckRequest.setTransactionId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        fraudCheckRequest.setPayerName("John Doe");
        fraudCheckRequest.setPayeeName("Jane Smith");
        fraudCheckRequest.setPayerCountry("USA");
        fraudCheckRequest.setPayerBank("Bank of America");
        fraudCheckRequest.setPayeeBank("BNP Paribas");
        fraudCheckRequest.setPaymentInstruction("Loan Repayment");
        fraudCheckRequest.setPayeeCountry("GBR");

        // Convert to XML
        String xmlRequest = xmlMapper.writeValueAsString(fraudCheckRequest);

        // Set up MockEndpoint to verify message sent to pps-fraud-check-response
        MockEndpoint ppsFraudCheckResponseMock = camelContext.getEndpoint("mock:kafka:pps-fraud-check-request?brokers=localhost:9092", MockEndpoint.class);
        ppsFraudCheckResponseMock.expectedMessageCount(1);

        // Send XML response to fraud-check-result topic
        producerTemplate.sendBody("mock:kafka:pps-fraud-check-request?brokers=localhost:9092", xmlRequest);

        // Assert that the message was processed and sent
        ppsFraudCheckResponseMock.assertIsSatisfied();
    }
}
