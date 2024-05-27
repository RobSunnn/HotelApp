package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class ContactUsView {

    private String name;

    private String email;

    private String phoneNumber;

    private String message;

    private LocalDateTime created;

    public ContactUsView() {}

    public String getName() {
        return name;
    }

    public ContactUsView setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactUsView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactUsView setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ContactUsView setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ContactUsView setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }
}
