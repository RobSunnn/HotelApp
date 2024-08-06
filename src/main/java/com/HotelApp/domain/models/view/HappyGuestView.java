package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class HappyGuestView {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private String documentId;
    private Integer lastRoomUsed;
    private Integer timesThatGuestHaveBeenToHotel;
    private LocalDateTime lastCheckIn;
    private LocalDateTime lastCheckOut;

    public HappyGuestView() {
    }

    public Long getId() {
        return id;
    }

    public HappyGuestView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public HappyGuestView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public HappyGuestView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public HappyGuestView setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public HappyGuestView setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public HappyGuestView setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Integer getLastRoomUsed() {
        return lastRoomUsed;
    }

    public HappyGuestView setLastRoomUsed(Integer lastRoomUsed) {
        this.lastRoomUsed = lastRoomUsed;
        return this;
    }

    public Integer getTimesThatGuestHaveBeenToHotel() {
        return timesThatGuestHaveBeenToHotel;
    }

    public HappyGuestView setTimesThatGuestHaveBeenToHotel(Integer timesThatGuestHaveBeenToHotel) {
        this.timesThatGuestHaveBeenToHotel = timesThatGuestHaveBeenToHotel;
        return this;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public HappyGuestView setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
        return this;
    }

    public LocalDateTime getLastCheckOut() {
        return lastCheckOut;
    }

    public HappyGuestView setLastCheckOut(LocalDateTime lastCheckOut) {
        this.lastCheckOut = lastCheckOut;
        return this;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
