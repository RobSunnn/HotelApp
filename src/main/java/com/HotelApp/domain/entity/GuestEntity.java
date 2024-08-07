package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import static com.HotelApp.common.constants.ValidationConstants.*;

@Entity
@Table(name = "guests")
public class GuestEntity extends BaseEntity {

    @Column(nullable = false)
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String lastName;

    @Column(nullable = false)
    @Size(max = 200, message = EMAIL_TOO_LONG)
    @NotBlank(message = EMAIL_NOT_BLANK)
    private String email;

    @Column(nullable = false)
    @NotNull(message = AGE_IS_REQUIRED)
    @Positive(message = NEGATIVE_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
    @Max(value = 100, message = INVALID_AGE_OVER_100)
    private Integer age;

    @Column(nullable = false, name = "document_id")
    @NotBlank(message = DOCUMENT_ID_EMPTY)
    @Size(max = 200, message = DOCUMENT_ID_TOO_LONG)
    private String documentId;

    @ManyToOne
    private RoomEntity room;

    @Column(nullable = false, name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(nullable = false, name = "check_out_time")
    private LocalDateTime checkOutTime;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

    public GuestEntity() {
    }

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

    public RoomEntity getRoom() {
        return room;
    }

    public GuestEntity setRoom(RoomEntity roomNumber) {
        this.room = roomNumber;
        return this;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public GuestEntity setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
        return this;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public GuestEntity setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
        return this;
    }

    public GuestEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
