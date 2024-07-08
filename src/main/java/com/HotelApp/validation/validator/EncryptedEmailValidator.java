package com.HotelApp.validation.validator;

import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EncryptedEmailValidator implements ConstraintValidator<ValidEmail, String> {
    private final EncryptionService encryptionService;

    public EncryptedEmailValidator(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public boolean isValid(String encryptedEmail, ConstraintValidatorContext context) {
        try {
            String decryptedEmail = encryptionService.decrypt(encryptedEmail);
            String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
            boolean matches = decryptedEmail.matches(emailPattern);
            return matches;
        } catch (Exception e) {
            return false;
        }
    }
}
