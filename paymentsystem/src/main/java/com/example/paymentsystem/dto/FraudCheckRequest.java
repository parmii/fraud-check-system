package com.example.paymentsystem.dto;


import com.example.paymentsystem.validator.ISO3CountryCode;
import com.example.paymentsystem.validator.ISO42117CurrencyCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Schema(name = "payment")
public class FraudCheckRequest {
    @NotNull(message = "Transaction id is required")
    private UUID transactionId;

    @NotNull(message = "Payee name id is required")
    private String payerName;

    @NotNull(message = "Payer name id is required")
    private String payeeName;

    @NotNull(message = "Payer's country id is required")
    @ISO3CountryCode
    private String payerCountry;

    @NotNull(message = "Payer's bank is required")
    private String payerBank;

    @NotNull(message = "Payee's bank id is required")
    private String payeeBank;

    @NotNull(message = "Payee's country id is required")
    @ISO3CountryCode
    @JsonProperty("payeeCountry")
    private String payeeCountry;

    @NotNull(message = "Transaction id is required")
    private String payeeAccount;

    private String paymentInstruction;
}
