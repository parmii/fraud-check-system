package com.example.paymentprocessor.repository;

import com.example.paymentsystem.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {
    @Id
  //  @GeneratedValue(strategy = GenerationType.UUID)
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

    private String status;  // Approved, Rejected

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    public String getPayerCountry() {
        return payerCountry;
    }

    public void setPayerCountry(String payerCountry) {
        this.payerCountry = payerCountry;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeBank() {
        return payeeBank;
    }

    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }

    public String getPayeeCountry() {
        return payeeCountry;
    }

    public void setPayeeCountry(String payeeCountry) {
        this.payeeCountry = payeeCountry;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getPaymentInstruction() {
        return paymentInstruction;
    }

    public void setPaymentInstruction(String paymentInstruction) {
        this.paymentInstruction = paymentInstruction;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Timestamp getCreatedTimetamp() {
        return createdTimetamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedTimetamp(Timestamp createdTimetamp) {
        this.createdTimetamp = createdTimetamp;
    }
}
