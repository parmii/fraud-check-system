package com.example.paymentsystem.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Blacklist {
    private String[] payers;
    private String[] payees;
    private String[] countries;
    private String[] banks;
    private String[] paymentInstructions;
}
