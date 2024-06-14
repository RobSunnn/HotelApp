package com.HotelApp.validation.validator;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.service.UserService;
import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String keyParam = request.getParameter("key"); // Change as per your annotation definition
            String ivParam = request.getParameter("iv");   // Change as per your annotation definition
            // Decrypt the value (assuming you have decryption logic in userService)
            String decryptedEmail = userService.decryptEmail(encryptedEmail, ivParam, keyParam);

            // Validate decrypted email
            String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
            return decryptedEmail.matches(emailPattern);
        } catch (Exception e) {
            return false;
        }
    }
}
