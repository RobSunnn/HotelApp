package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.*;

public class AddGuestBindingModel {

    @NotBlank(message = "You need to add the first name of the guest.")
    private String firstName;

    @NotBlank(message = "You need to add the last name of the guest.")
    private String lastName;

    @Email(message = "The guest want to leave their email, so put it in a correct way.")
    private String email;

    @Positive(message = "Age cannot be negative!")
    @NotNull(message = "Please enter the age of the guest.")
    @Min(value = 18, message = "Guest must be over 18 years old")
    private Integer age;

    @NotBlank(message = "We need the document id of the guest.")
    private String documentId;

    @NotNull(message = "Room number is required")
    private Integer roomNumber;

    @NotNull(message = "You should enter the days that guest want to stay.")
    @Positive(message = "No negative days")
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
