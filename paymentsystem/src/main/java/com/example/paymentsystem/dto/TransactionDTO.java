package com.example.paymentsystem.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;


@Data
public class TransactionDTO {
    private UUID id;
    private double amount;
    private int payeeId;
    private int payerId;
    private int currencyId;
    private Timestamp executeionDate;
    private Timestamp updatedDate;
    private PaymentStatus status;
}
