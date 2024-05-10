package com.HotelApp.domain.models.view;

import jakarta.persistence.Column;

public class GuestView {

    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    private String documentId;

    private Integer roomNumber;

    public GuestView() {}

    public String getFirstName() {
        return firstName;
    }

    public GuestView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public GuestView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GuestView setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public GuestView setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public GuestView setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public GuestView setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }
}
