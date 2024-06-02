package com.HotelApp.domain.models.binding;

public class EditUserProfileBindingModel {
//TODO: VALIDATION
    private String firstName;

    private String lastName;

    private String email;

    private Integer age;

    public EditUserProfileBindingModel() {}

    public String getFirstName() {
        return firstName;
    }

    public EditUserProfileBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EditUserProfileBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EditUserProfileBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public EditUserProfileBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }
}
