package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_requests")
public class ContactRequestEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime created;

    private Boolean isChecked;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

    public ContactRequestEntity() {
    }

    public String getName() {
        return name;
    }

    public ContactRequestEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactRequestEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ContactRequestEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ContactRequestEntity setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ContactRequestEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public ContactRequestEntity setChecked(Boolean checked) {
        isChecked = checked;
        return this;
    }

    public ContactRequestEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
