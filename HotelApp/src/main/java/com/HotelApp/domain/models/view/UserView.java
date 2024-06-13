package com.HotelApp.domain.models.view;

import com.HotelApp.domain.entity.RoleEntity;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class UserView {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

    private String encryptedEmail;

    private Integer age;

    private byte[] userImage;

    private List<RoleEntity> roles;

    public UserView() {
    }

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

    public String getFirstName() {
        return firstName;
    }

    public UserView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEncryptedEmail() {
        return encryptedEmail;
    }

    public UserView setEncryptedEmail(String encryptedEmail) {
        this.encryptedEmail = encryptedEmail;
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

    public byte[] getUserImage() {
        return userImage;
    }

    public UserView setUserImage(byte[] userImage) {
        this.userImage = userImage;
        return this;
    }

    public String getProfilePictureBase64() {
        return userImage != null ? Base64.getEncoder().encodeToString(userImage) : "";
    }

    public String getRoleNames() {
        return roles.stream()
                .map(roleEntity -> roleEntity.getName().name())
                .collect(Collectors.joining(", "));
    }
}
