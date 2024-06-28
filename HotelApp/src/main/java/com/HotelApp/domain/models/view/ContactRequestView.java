package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class ContactRequestView {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String message;

    private LocalDateTime created;

    private Boolean isChecked;

    public ContactRequestView() {}

    public Long getId() {
        return id;
    }

    public ContactRequestView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ContactRequestView setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactRequestView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactRequestView setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ContactRequestView setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ContactRequestView setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public ContactRequestView setChecked(Boolean checked) {
        isChecked = checked;
        return this;
    }
}
