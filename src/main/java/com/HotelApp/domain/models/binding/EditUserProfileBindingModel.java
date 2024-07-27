package com.HotelApp.domain.models.binding;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.*;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class EditUserProfileBindingModel {

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
    private String firstName;

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
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

}
