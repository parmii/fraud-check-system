package com.example.paymentsystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

import java.util.Currency;
import java.util.Set;

public class ISO4217CurrencyCodeValidator implements ConstraintValidator<ISO42117CurrencyCode, String> {
private Boolean isOptional;

@Override
public void initialize(ISO42117CurrencyCode currencyCode) {
    this.isOptional = currencyCode.optional();
}

@Override
public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    boolean containsIsoCode = false;

    Set<Currency> currencies = Currency.getAvailableCurrencies();
    try {
        containsIsoCode = currencies.contains(Currency.getInstance(value));
    }catch(IllegalArgumentException e){
    }
    return isOptional ? (containsIsoCode || (Strings.isEmpty(value))) : containsIsoCode;
}
}
