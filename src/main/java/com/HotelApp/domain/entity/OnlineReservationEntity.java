package com.HotelApp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "online_reservations")
public class OnlineReservationEntity extends BaseEntity {

    private String fullName;

    private String email;

    private Integer age;

    @Size(max = 400)
    private String additionalInfo;

    private LocalDateTime timestamp;

    private boolean isChecked;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

    //TODO: phone number after you put it in the user entity

    public OnlineReservationEntity() {
    }

    public String getFullName() {
        return fullName;
    }

    public OnlineReservationEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public OnlineReservationEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public OnlineReservationEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public OnlineReservationEntity setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public OnlineReservationEntity setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public OnlineReservationEntity setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }

    public OnlineReservationEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
