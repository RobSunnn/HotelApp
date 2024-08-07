package com.HotelApp.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

import static com.HotelApp.common.constants.ValidationConstants.*;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, name = "first_name")
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
    private String firstName;

    @Column(nullable = false, name = "last_name")
    @NotBlank(message = NAME_BLANK)
    @Size(min = 2, message = NAME_LENGTH_TOO_SHORT)
    @Size(max = 60, message = NAME_LENGTH_TOO_LONG)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Size(max = 200, message = EMAIL_TOO_LONG)
    @NotBlank(message = EMAIL_NOT_BLANK)
    private String email;

    @Column(nullable = false)
    @NotNull(message = AGE_IS_REQUIRED)
    @Positive(message = NEGATIVE_AGE)
    @Min(value = 18, message = MINIMUM_AGE)
    @Max(value = 100, message = INVALID_AGE_OVER_100)
    private Integer age;

    @Column(nullable = false)
    @NotBlank(message = EMPTY_PASSWORD)
    @Size(min = 5, message = PASSWORD_LENGTH)
    private String password;

    @Lob
    private Blob userImage;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<RoleEntity> roles;

    @Column(nullable = false)
    private LocalDateTime created;

    public UserEntity() {
    }

    public Integer getAge() {
        return age;
    }

    public UserEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public UserEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public Blob getUserImage() {
        return userImage;
    }

    public UserEntity setUserImage(Blob userImage) {
        this.userImage = userImage;
        return this;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public UserEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
