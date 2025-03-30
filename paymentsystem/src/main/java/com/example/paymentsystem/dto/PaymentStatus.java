package com.example.paymentsystem.dto;

public enum PaymentStatus {
    INITIATED("INITIATED"),
    FAILED("FAILED"),
    APPROVED("APPROVED"),
    FRAUD_CHECK_INITAITED("FRAUD_CHECK_INITAITED"),
    REJECTED("REJECTED"),
    FRAUD_CHECK_SUCCESS("FRAUD_CHECK_SUCCESS");

    private String status;

    PaymentStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
