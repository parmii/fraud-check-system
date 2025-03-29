package com.example.fraudcheck;

import com.example.fraudcheck.service.FraudCheckService;
import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.utils.BlackListValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

class FraudCheckServiceTest {

    private FraudCheckService fraudCheckService;
    private FraudCheckRequest fraudCheckRequest;

    @BeforeEach
    void setUp() {
        fraudCheckService = new FraudCheckService();

        // Initialize a sample fraudCheckRequest
        fraudCheckRequest = new FraudCheckRequest();
        fraudCheckRequest.setTransactionId(UUID.randomUUID());
        fraudCheckRequest.setPayerName("John Doe");
        fraudCheckRequest.setPayeeName("Jane Smith");
        fraudCheckRequest.setPayerCountry("USA");
        fraudCheckRequest.setPayerBank("Bank of America");
        fraudCheckRequest.setPayeeBank("BNP Paribas");
        fraudCheckRequest.setPaymentInstruction("Loan Repayment");
        fraudCheckRequest.setPayeeCountry("GBR");
    }

    @Test
    void testCheckFraud_ValidTransaction() {
        try (MockedStatic<BlackListValidator> mockedValidator = mockStatic(BlackListValidator.class)) {
            // Mock BlackListValidator to return true (valid transaction)
            mockedValidator.when(() -> BlackListValidator.validate(fraudCheckRequest)).thenReturn(true);

            FraudCheckResponse response = fraudCheckService.checkFraud(fraudCheckRequest);

            assertEquals(1, response.getStatus(), "Expected status to be 1 for a valid transaction.");
            assertEquals("Nothing found, all okay.", response.getMessage(), "Expected success message.");
            assertEquals(fraudCheckRequest.getTransactionId(), response.getTransactionId(), "Transaction ID should match.");
        }
    }

    @Test
    void testCheckFraud_SuspiciousTransaction() {
        try (MockedStatic<BlackListValidator> mockedValidator = mockStatic(BlackListValidator.class)) {
            // Mock BlackListValidator to return false (suspicious transaction)
            mockedValidator.when(() -> BlackListValidator.validate(fraudCheckRequest)).thenReturn(false);

            FraudCheckResponse response = fraudCheckService.checkFraud(fraudCheckRequest);

            assertEquals(0, response.getStatus(), "Expected status to be 0 for a suspicious transaction.");
            assertEquals("Suspicious payment", response.getMessage(), "Expected fraud warning message.");
            assertEquals(fraudCheckRequest.getTransactionId(), response.getTransactionId(), "Transaction ID should match.");
        }
    }
}
