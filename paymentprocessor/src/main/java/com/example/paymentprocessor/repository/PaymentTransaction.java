package com.example.paymentprocessor.repository;

import com.example.paymentsystem.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    private String payerName;
    private String payerBank;
    private String payerCountry;
    private String payerAccount;

    private String payeeName;
    private String payeeBank;
    private String payeeCountry;
    private String payeeAccount;

    private String paymentInstruction;
    private String currency;
    private double amount;
    private Date executionDate;
    private Timestamp createdTimetamp;

    private PaymentStatus status;  // Approved, Rejected
}
