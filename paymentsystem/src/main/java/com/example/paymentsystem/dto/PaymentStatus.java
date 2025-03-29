package com.example.paymentsystem.dto;

public enum PaymentStatus {
    INITIATED("INITIATED"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS"),
    FRAUD_CHECK_INITAITED("FRAUD_CHECK_INITAITED"),
    FRAUD_CHECK_FAILED("FRAUD_CHECK_FAILED"),
    FRAUD_CHECK_SUCCESS("FRAUD_CHECK_SUCCESS");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
