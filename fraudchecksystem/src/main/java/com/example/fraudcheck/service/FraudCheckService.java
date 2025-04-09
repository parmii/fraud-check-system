package com.example.fraudcheck.service;

import com.example.paymentsystem.dto.FraudCheckResponse;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.utils.BlackListValidator;
import com.example.paymentsystem.utils.JSONMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class FraudCheckService {

    public FraudCheckResponse checkFraud(FraudCheckRequest payment) {
        boolean validTransaction = BlackListValidator.validate(payment);

        FraudCheckResponse fraudCheckResponse = new FraudCheckResponse();
        if(validTransaction) {
            fraudCheckResponse.setTransactionId(payment.getTransactionId());
            fraudCheckResponse.setMessage("Nothing found, all okay.");
            fraudCheckResponse.setStatus(1);
        } else {
            fraudCheckResponse.setTransactionId(payment.getTransactionId());
            fraudCheckResponse.setMessage("Suspicious payment");
            fraudCheckResponse.setStatus(0);
        }

        return fraudCheckResponse;
    }
}
