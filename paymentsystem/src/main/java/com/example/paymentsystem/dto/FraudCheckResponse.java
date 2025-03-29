package com.example.paymentsystem.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.UUID;

@Data
@XmlRootElement
public class FraudCheckResponse {
    private UUID transactionId;
    private String message;
    private int status;
}
