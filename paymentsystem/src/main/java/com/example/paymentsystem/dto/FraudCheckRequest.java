package com.example.paymentsystem.dto;

import com.example.paymentsystem.validator.ISO3CountryCode;
import com.example.paymentsystem.validator.ISO42117CurrencyCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "payment")
public class FraudCheckRequest {
    @NotNull(message = "Transaction id is required")
    @JsonProperty("transactionId")
    private UUID transactionId;

    @NotNull(message = "Payee name id is required")
    @JsonProperty("payerName")
    private String payerName;

    @JsonProperty("payeeName")
    @NotNull(message = "Payer name id is required")
    private String payeeName;

    @NotNull(message = "Payer's country id is required")
    @JsonProperty("payerCountry")
    @ISO3CountryCode
    private String payerCountry;

    @NotNull(message = "Payer's bank is required")
    @JsonProperty("payerBank")
    private String payerBank;

    @NotNull(message = "Payee's bank id is required")
    @JsonProperty("payeeBank")
    private String payeeBank;

    @NotNull(message = "Payee's country id is required")
    @ISO3CountryCode
    @JsonProperty("payeeCountry")
    private String payeeCountry;

    @JsonProperty("paymentInstruction")
    private String paymentInstruction;
}
