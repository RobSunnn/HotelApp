package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.HotelApp.common.constants.ValidationConstants.EMAIL_NOT_BLANK;
import static com.HotelApp.common.constants.ValidationConstants.EMAIL_TOO_LONG;

@Entity
@Table(name = "subscribers")
public class SubscriberEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    @NotBlank(message = EMAIL_NOT_BLANK)
    @Size(max = 200, message = EMAIL_TOO_LONG)
    private String email;

    @Column(nullable = false)
    private LocalDateTime timeOfSubscription;

    @Column(nullable = false)
    private Integer counterOfSubscriptions;

    @ManyToOne
    private HotelInfoEntity hotelInfoEntity;

    public SubscriberEntity() {
    }

    public String getEmail() {
        return email;
    }

    public SubscriberEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public LocalDateTime getTimeOfSubscription() {
        return timeOfSubscription;
    }

    public SubscriberEntity setTimeOfSubscription(LocalDateTime timeOfSubscription) {
        this.timeOfSubscription = timeOfSubscription;
        return this;
    }

    public Integer getCounterOfSubscriptions() {
        return counterOfSubscriptions;
    }

    public SubscriberEntity setCounterOfSubscriptions(Integer counterOfSubscriptions) {
        this.counterOfSubscriptions = counterOfSubscriptions;
        return this;
    }

    public SubscriberEntity setHotelInfoEntity(HotelInfoEntity hotelInfoEntity) {
        this.hotelInfoEntity = hotelInfoEntity;
        return this;
    }
}
