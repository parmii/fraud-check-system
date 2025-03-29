package com.example.paymentsystem.dto;

import lombok.Data;

@Data
public class Blacklist {
    private String[] payers;
    private String[] payees;
    private String[] countries;
    private String[] banks;
    private String[] paymentInstructions;
}
