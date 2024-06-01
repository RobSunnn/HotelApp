package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactRequestBindingModel {
    @NotBlank(message = "Enter your name.")
    @Size(min = 2, message = "Your name must be at least 2 characters.")
    @Size(max = 30, message = "Your name is too long...")
    private String name;

    @NotBlank(message = "Enter your email.")
    @Email(message = "Enter a valid email...")
    @Size(max = 100, message = "Your email is too long!")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Your message should not be blank.")
    @Size(min = 2, message = "At least say hi.")
    @Size(max = 400, message = "Your message is too long.")
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
