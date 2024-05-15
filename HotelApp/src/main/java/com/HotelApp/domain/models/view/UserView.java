package com.HotelApp.domain.models.view;

public class UserView {

    private String fullName;

    private String email;

    private Integer age;

    public UserView() {}

    public String getFullName() {
        return fullName;
    }

    public UserView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserView setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserView setAge(Integer age) {
        this.age = age;
        return this;
    }
}
