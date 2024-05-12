package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "happy_guests")
public class HappyGuestEntity extends BaseEntity {

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

    @Column(nullable = false, name = "last_room_used")
    private Integer lastRoomUsed;

    @Column(nullable = false, name = "times_that_guest_have_been_to_hotel")
    private Integer timesThatGuestHaveBeenToHotel;

    @Column(nullable = false, name = "last_check_in")
    private LocalDateTime lastCheckIn;

    @Column(nullable = false, name = "last_check_out")
    private LocalDateTime lastCheckOut;

    public HappyGuestEntity() {
    }

    public String getFirstName() {
        return firstName;
    }

    public HappyGuestEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public HappyGuestEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public HappyGuestEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public HappyGuestEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public HappyGuestEntity setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Integer getLastRoomUsed() {
        return lastRoomUsed;
    }

    public HappyGuestEntity setLastRoomUsed(Integer lastRoomUsed) {
        this.lastRoomUsed = lastRoomUsed;
        return this;
    }

    public Integer getTimesThatGuestHaveBeenToHotel() {
        return timesThatGuestHaveBeenToHotel;
    }

    public HappyGuestEntity setTimesThatGuestHaveBeenToHotel(Integer timesThatGuestHaveBeenToHotel) {
        this.timesThatGuestHaveBeenToHotel = timesThatGuestHaveBeenToHotel;
        return this;
    }

    public LocalDateTime getLastCheckIn() {
        return lastCheckIn;
    }

    public HappyGuestEntity setLastCheckIn(LocalDateTime lastCheckIn) {
        this.lastCheckIn = lastCheckIn;
        return this;
    }

    public LocalDateTime getLastCheckOut() {
        return lastCheckOut;
    }

    public HappyGuestEntity setLastCheckOut(LocalDateTime lastCheckOut) {
        this.lastCheckOut = lastCheckOut;
        return this;
    }
}
