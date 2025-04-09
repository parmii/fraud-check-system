package com.example.paymentsystem.utils;

import com.example.paymentsystem.dto.Blacklist;
import com.example.paymentsystem.dto.FraudCheckRequest;
import com.example.paymentsystem.dto.Payment;
import com.fasterxml.jackson.core.type.TypeReference;

public class BlackListValidator {

    public static boolean validate(FraudCheckRequest fraudCheckRequest) {
        StringBuilder vaidationError = new StringBuilder();
        if(fraudCheckRequest != null) {
            Blacklist blacklist = ResourceMapper.get("blacklist.json", new TypeReference<Blacklist>() {
            });
            if(validateProperty(blacklist.getBanks(),fraudCheckRequest.getPayeeBank())) {
                return false;
            }
            if(validateProperty(blacklist.getBanks(),fraudCheckRequest.getPayerBank())) {
                return false;
            }
            if(validateProperty(blacklist.getPayees(),fraudCheckRequest.getPayeeName())) {
                return false;
            }
            if(validateProperty(blacklist.getPayers(),fraudCheckRequest.getPayerName())) {
                return false;
            }
            if(validateProperty(blacklist.getCountries(),fraudCheckRequest.getPayerCountry())) {
                return false;
            }
            if(validateProperty(blacklist.getCountries(),fraudCheckRequest.getPayeeCountry())) {
                return false;
            }
            if(validateProperty(blacklist.getPaymentInstructions(),fraudCheckRequest.getPaymentInstruction())) {
                return false;
            }
        }
        return true;
    }

    private static boolean validateProperty(String[] blackListValues, String valueToValidate) {
        boolean blacklisted = false;
        if(blackListValues != null && blackListValues.length > 0) {
            for (String value : blackListValues) {
                if(value.equalsIgnoreCase(valueToValidate)) {
                    blacklisted = true;
                }
            }
        }

        return blacklisted;
    }


}
