package com.HotelApp.validation.validator;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.HotelApp.util.encryptionUtil.EncryptionUtil.decrypt;

public class EncryptedEmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Override
    public boolean isValid(String encryptedEmail, ConstraintValidatorContext context) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String keyParam = request.getParameter("key");
            String ivParam = request.getParameter("iv");

            String decryptedEmail = decrypt(encryptedEmail, ivParam, keyParam);

            String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
            return decryptedEmail.matches(emailPattern);
        } catch (Exception e) {
            return false;
        }
    }
}
