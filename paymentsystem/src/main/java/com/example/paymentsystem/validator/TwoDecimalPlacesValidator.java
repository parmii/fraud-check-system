package com.example.paymentsystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class TwoDecimalPlacesValidator implements ConstraintValidator<TwoDecimalPlaces, Double> {
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]{2})?$");

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // If it's nullable, consider it valid
        }

        // Ensure that we don't have more than 2 decimal places
        return DECIMAL_PATTERN.matcher(value.toString()).matches();
    }
}
