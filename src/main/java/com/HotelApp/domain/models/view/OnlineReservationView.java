package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class OnlineReservationView {

    private Long id;

    private String fullName;

    private String email;

    private Integer age;

    private String additionalInfo;

    private LocalDateTime timestamp;

    private boolean isChecked;

    public OnlineReservationView() {
    }

    public Long getId() {
        return id;
    }

    public OnlineReservationView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public OnlineReservationView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public OnlineReservationView setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public OnlineReservationView setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public OnlineReservationView setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public OnlineReservationView setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public OnlineReservationView setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }
}
