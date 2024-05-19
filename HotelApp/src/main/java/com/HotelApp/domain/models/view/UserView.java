package com.HotelApp.domain.models.view;

import com.HotelApp.domain.entity.RoleEntity;

import java.util.List;

public class UserView {

    private Long id;

    private String fullName;

    private String email;

    private Integer age;

    private List<RoleEntity> roles;

    public UserView() {}

    public String getFullName() {
        return fullName;
    }

    public UserView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserView setId(Long id) {
        this.id = id;
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

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserView setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}
