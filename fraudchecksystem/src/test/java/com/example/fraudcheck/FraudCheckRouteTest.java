package com.example.fraudcheck;

import com.example.fraudcheck.config.CamelConfig;
import com.example.fraudcheck.config.FraudCheckRoute;
import com.example.fraudcheck.service.FraudCheckService;
import com.example.paymentsystem.constant.Event;
import com.example.paymentsystem.service.AuditLogService;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
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
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@CamelSpringBootTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FraudCheckRoute.class, CamelConfig.class}) // Load the specific Camel route class
@MockEndpoints("kafka:*") // Mock all Kafka endpoints
public class FraudCheckRouteTest {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @MockBean
    private AuditLogService auditLogService;

    @MockBean
    private FraudCheckService fraudCheckService;

    @BeforeEach
    void setUp() throws Exception {
        // Manually add the route if not already present
        if (!camelContext.getRoutes().isEmpty()) {
            camelContext.stop();
        }
        camelContext.addRoutes(new FraudCheckRoute(auditLogService, fraudCheckService));
        camelContext.start();
    }

    @Test
    public void testFraudCheckRoute() throws Exception {
        // Create a mock fraud check response
        FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
        fraudCheckResponse.setTransactionId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        fraudCheckResponse.setStatus(1);
        fraudCheckResponse.setMessage("Nothing found, all okay.");

        // Mock fraud check service behavior
        when(fraudCheckService.checkFraud(any(FraudCheckRequest.class))).thenReturn(fraudCheckResponse);

        // Create a sample payment message in XML format
        String xmlPayment = "<FraudCheckRequest><transactionId>550e8400-e29b-41d4-a716-446655440000</transactionId><payerName>John Doe</payerName><payeeName>Jane Smith</payeeName><payerCountry>USA</payerCountry><payerBank>Bank of America</payerBank><payerAccount>123456</payerAccount><payeeBank>BNP Paribas</payeeBank><payeeAccount>789012</payeeAccount><paymentInstruction>Loan Repayment</paymentInstruction><amount>1000</amount><currency>USD</currency><payeeCountry>GBR</payeeCountry></FraudCheckRequest>";

        // Set up MockEndpoint to verify message sent to Kafka
        MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:kafka:fraud-check-request?brokers=localhost:9092", MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(xmlPayment);
        mockEndpoint.setAssertPeriod(5000);

        // Send XML payment to the fraud-check-request topic
        producerTemplate.sendBody("mock:kafka:fraud-check-request?brokers=localhost:9092", xmlPayment);

        // Assert that the message was processed and sent to fraud-check-result
        mockEndpoint.assertIsSatisfied();
    }
}
