package com.example.paymentprocessor.service;


import com.example.paymentsystem.dto.Payment;
import com.example.paymentsystem.dto.PaymentResponse;

public interface BrokerService {
    public PaymentResponse sendRequest(Payment payment);
}
