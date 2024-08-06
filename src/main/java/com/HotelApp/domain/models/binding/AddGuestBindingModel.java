package com.HotelApp.domain.models.binding;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.*;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class AddGuestBindingModel {

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String firstName;

    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 30, message = NAME_LENGTH_TOO_LONG)
    private String lastName;

    @ValidEmail(message = INVALID_EMAIL)
    private String email;

    @Positive(message = NEGATIVE_AGE)
    @NotNull(message = INVALID_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
    @Max(value = 100, message = INVALID_AGE_OVER_100)
    private Integer age;

    @NotBlank(message = DOCUMENT_ID_EMPTY)
    @Size(max = 200, message = DOCUMENT_ID_TOO_LONG)
    private String documentId;

    @NotNull(message = ROOM_NUMBER_REQUIRED)
    private Integer roomNumber;

    @NotNull(message = EMPTY_DAYS)
    @Positive(message = NEGATIVE_DAYS)
    private Integer daysToStay;

    public AddGuestBindingModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public AddGuestBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AddGuestBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AddGuestBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AddGuestBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public AddGuestBindingModel setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public AddGuestBindingModel setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public Integer getDaysToStay() {
        return daysToStay;
    }

    public AddGuestBindingModel setDaysToStay(Integer daysToStay) {
        this.daysToStay = daysToStay;
        return this;
    }


}
