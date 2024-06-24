package com.HotelApp.validation.validator;

import com.HotelApp.service.UserService;
import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class EncryptedEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final UserService userService;

    public EncryptedEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String encryptedEmail, ConstraintValidatorContext context) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String keyParam = request.getParameter("key");
            String ivParam = request.getParameter("iv");

            String decryptedEmail = userService.decrypt(encryptedEmail, ivParam, keyParam);

            String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
            return decryptedEmail.matches(emailPattern);
        } catch (Exception e) {
            return false;
        }
    }
}
