package com.HotelApp.domain.models.binding;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.*;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class EditUserProfileBindingModel {

    @NotBlank(message = FIRST_AND_LAST_NAME_NOT_BLANK)
    @Size(min = 2, message = FIRST_AND_LAST_NAME_LENGTH)
    @Size(max = 60, message = FIRST_AND_LAST_NAME_LENGTH_TOO_LONG)
    private String firstName;

    @NotBlank(message = FIRST_AND_LAST_NAME_NOT_BLANK)
    @Size(min = 2, message = FIRST_AND_LAST_NAME_LENGTH)
    @Size(max = 60, message = FIRST_AND_LAST_NAME_LENGTH_TOO_LONG)
    private String lastName;

    @NotBlank(message = EMAIL_NOT_BLANK)
    @ValidEmail(message = INVALID_EMAIL)
//    @Size(max = 100, message = EMAIL_TOO_LONG)
    private String email;

    @NotNull(message = INVALID_AGE)
    @Positive(message = NEGATIVE_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
    @Max(value = 100, message = INVALID_AGE_OVER_100)
    private Integer age;

    private String iv;

    private String key;

    public EditUserProfileBindingModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public EditUserProfileBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EditUserProfileBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EditUserProfileBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public EditUserProfileBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getIv() {
        return iv;
    }

    public EditUserProfileBindingModel setIv(String iv) {
        this.iv = iv;
        return this;
    }

    public String getKey() {
        return key;
    }

    public EditUserProfileBindingModel setKey(String key) {
        this.key = key;
        return this;
    }
}
