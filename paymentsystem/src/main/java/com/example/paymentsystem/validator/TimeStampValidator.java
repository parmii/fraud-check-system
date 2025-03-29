package com.example.paymentsystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampValidator implements ConstraintValidator<ValidTimeStamp, String> {

    private Boolean isOptional;

    @Override
    public void initialize(ValidTimeStamp timestamp) {
        this.isOptional = timestamp.optional();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        boolean validTimeStampFormat = isValidFormat("YYYY-MM-DDThh:mm:ssZ", value);

        return isOptional ? (validTimeStampFormat || (Strings.isEmpty(value))) : validTimeStampFormat;
    }

    private static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (value != null){
                date = sdf.parse(value);
                if (!value.equals(sdf.format(date))) {
                    date = null;
                }
            }

        } catch (ParseException ex) {
        }
        return date != null;
    }
}