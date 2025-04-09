package com.example.brokersystem;


import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.PaymentResponse;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "app.fcs.requestroute=kafka:fraud-check-request?brokers=localhost:9092",
        "app.fcs.responseroute=kafka:fraud-check-result?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest",
        "app.pps.requestroute=kafka:pps-fraud-check-request?brokers=localhost:9092&groupId=fraud-check-consumer&autoOffsetReset=earliest",
        "app.pps.responseroute=kafka:pps-fraud-check-response?brokers=localhost:9092"
})
public class FraudCheckControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CamelContext camelContext;

    @BeforeEach
    public void setup() throws Exception {
        camelContext.start();  // Ensure CamelContext is started before running tests
    }

    @Test
    public void testSubmitFraudCheck() {
        Calendar calendar = Calendar.getInstance();
        FraudCheckRequest request = new FraudCheckRequest();
        request.setTransactionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        request.setPayerName("John Doe");
        request.setPayerBank("Bank of America");
        request.setPayerCountry("USA");
        request.setPayeeName("Jane Doe");
        request.setPayeeBank("BNP Paribas");
        request.setPayeeCountry("GBR");
        request.setPaymentInstruction("Loan Repayment");

        HttpEntity<FraudCheckRequest> requestEntity = new HttpEntity<>(request);
        ResponseEntity<PaymentResponse> response = restTemplate.exchange(
                "/api/fraud-check/request", HttpMethod.POST, requestEntity, PaymentResponse.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("FraudCheckRequest fraud check request submitted successfully.", response.getBody().getMessage());
    }
}
