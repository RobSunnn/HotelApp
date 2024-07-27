package com.HotelApp.domain.models.binding;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class ContactRequestBindingModel {
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String name;
    //todo: check email size
    @NotBlank(message = EMAIL_NOT_BLANK)
    @ValidEmail(message = INVALID_EMAIL)
//    @Size(max = 100, message = "Your email is too long!")
    private String email;

    private String phoneNumber;

    @NotBlank(message = MESSAGE_BLANK)
    @Size(min = 2, message = MESSAGE_TOO_SHORT)
    @Size(max = 400, message = MESSAGE_TOO_LONG)
    private String message;


    public ContactRequestBindingModel() {
    }

    public String getName() {
        return name;
    }

    public ContactRequestBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactRequestBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactRequestBindingModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ContactRequestBindingModel setMessage(String message) {
        this.message = message;
        return this;
    }

}
