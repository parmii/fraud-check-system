package com.example.paymentsystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class TwoDecimalPlacesValidator implements ConstraintValidator<TwoDecimalPlaces, BigDecimal> {

    private static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+\\.\\d{2}");

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation if required
        }

        // Convert BigDecimal to String and check if it matches the pattern
        return DECIMAL_PATTERN.matcher(value.toPlainString()).matches();
    }
}
