package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "guests")
public class GuestEntity extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column //TODO: think if we need the email?
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true, name = "document_id")
    private String documentId;

    @Column(nullable = false, name = "room_number")
    private Integer roomNumber;

    public GuestEntity() {}

    public String getFirstName() {
        return firstName;
    }

    public GuestEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public GuestEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GuestEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public GuestEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public GuestEntity setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public GuestEntity setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }
}
