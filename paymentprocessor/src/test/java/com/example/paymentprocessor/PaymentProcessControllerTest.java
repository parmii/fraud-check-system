package com.example.paymentprocessor;

import com.example.paymentprocessor.controller.PaymentProcessController;
import com.example.paymentprocessor.repository.PaymentTransactionRepository;
import com.example.paymentprocessor.repository.TransactionRepository;
import com.example.paymentprocessor.service.BrokerService;
import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.dto.PaymentResponse;
import com.example.paymentsystem.dto.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentProcessController.class)
class PaymentProcessControllerTest {

    @Autowired
    private MockMvc mockMvc; //Simulates HTTP requests

    @Mock
    private BrokerService brokerServiceRest;

    @Mock
    private BrokerService brokerServiceMessanger;

    @MockBean
    private PaymentTransactionRepository repository;

    @MockBean
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentProcessController paymentProcessController; //Injects the mock into the controller

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentProcessController).build();
    }

    @Test
    void testProcessPayment() throws Exception {
        Calendar calendar = Calendar.getInstance();
        //Mock Payment and Response
        Payment payment = new Payment();
        payment.setTransactionId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        payment.setPayerName("John Doe");
        payment.setPayerBank("Bank of America");
        payment.setPayerCountry("USA");
        payment.setPayerAccount("123456");
        payment.setPayeeName("Jane Doe");
        payment.setPayeeBank("BNP Paribas");
        payment.setPayeeCountry("GBR");
        payment.setPayeeAccount("789012");
        payment.setPaymentInstruction("Loan Repayment");
        payment.setExecutionDate(new Date(calendar.getTimeInMillis()));
        payment.setAmount(1000.99);
        payment.setCurrency("USD");
        payment.setCreatedTimetamp(new Timestamp(calendar.getTimeInMillis()));
        // Convert Payment object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String paymentJson = objectMapper.writeValueAsString(payment);

        PaymentResponse response = new PaymentResponse();
        response.setTransactionId(payment.getTransactionId());
        response.setMessage("Payment sent for Fraud Check.");

        when(brokerServiceRest.sendRequest(any(Payment.class))).thenReturn(response);
        when(brokerServiceMessanger.sendRequest(any(Payment.class))).thenReturn(response);

        // Perform HTTP Request and Validate Response
        mockMvc.perform(post("/api/processpayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJson)) // Minimal JSON
                .andExpect(status().isOk());

        // Perform HTTP Request and Validate Response
        mockMvc.perform(post("/api/processpayment/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentJson)) // Minimal JSON
                .andExpect(status().isOk());

        verify(brokerServiceRest, times(1)).sendRequest(any(Payment.class)); // Ensure service was called

        verify(brokerServiceMessanger, times(1)).sendRequest(any(Payment.class)); // Ensure service was called
    }
}
