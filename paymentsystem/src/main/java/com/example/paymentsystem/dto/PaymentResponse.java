package com.example.paymentsystem.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentResponse {
    private UUID transactionId;
    private String message;
}
