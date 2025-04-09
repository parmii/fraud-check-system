package com.example.paymentsystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TwoDecimalPlacesValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TwoDecimalPlaces {
    
    String message() default "Number must have exactly two decimal places";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
