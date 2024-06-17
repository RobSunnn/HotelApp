package com.HotelApp.validation.annotation;

import com.HotelApp.validation.validator.EncryptedEmailValidator;
import jakarta.validation.Constraint;

import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EncryptedEmailValidator.class)
public @interface ValidEmail {
    String message() default "Invalid email.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}



