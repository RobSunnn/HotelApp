package com.HotelApp.domain.models.binding;


import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.*;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class UserRegisterBindingModel {

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
    private String firstName;

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
    private String lastName;

    @ValidEmail(message = INVALID_EMAIL)
    private String email;

    @NotNull(message = INVALID_AGE)
    @Positive(message = NEGATIVE_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
    @Max(value = 100, message = INVALID_AGE_OVER_100)
    private Integer age;

    @NotBlank(message = EMPTY_PASSWORD)
    @Size(min = 5, message = PASSWORD_LENGTH)
    private String password;

    @NotBlank(message = CONFIRM_PASSWORD)
    private String confirmPassword;

    public UserRegisterBindingModel() {
    }

    public Integer getAge() {
        return age;
    }

    public UserRegisterBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

}
