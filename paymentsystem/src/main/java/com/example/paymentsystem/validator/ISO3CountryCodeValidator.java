package com.example.paymentsystem.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;

public class ISO3CountryCodeValidator implements ConstraintValidator<ISO3CountryCode, String> {

    private ISO3CountryCode.IsoCountryCodeType type;

    @Override
    public void initialize(ISO3CountryCode constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Locale.IsoCountryCode code = switch (type) {
            case ALPHA2 -> Locale.IsoCountryCode.PART1_ALPHA2;
            case ALPHA3 -> Locale.IsoCountryCode.PART1_ALPHA3;
        };

        return Locale.getISOCountries(code).contains(value.toUpperCase());
    }
}