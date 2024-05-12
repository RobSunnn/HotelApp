package com.HotelApp.domain.models.binding;


import jakarta.validation.constraints.*;

import static com.HotelApp.validation.constants.ValidationConstants.*;

public class UserRegisterBindingModel {
    //TODO: validation maybe on fields or maybe with annotation

    @NotBlank(message = FIRST_AND_LAST_NAME_NOT_BLANK)
    @Size(min = 3, message = FIRST_AND_LAST_NAME_LENGTH)
    private String firstName;


    @NotBlank(message = FIRST_AND_LAST_NAME_NOT_BLANK)
    @Size(min = 3, message = FIRST_AND_LAST_NAME_LENGTH)
    private String lastName;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @Email(message = VALID_EMAIL)
    private String email;

    @NotNull(message = INVALID_AGE)
    @Positive(message = NEGATIVE_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
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
