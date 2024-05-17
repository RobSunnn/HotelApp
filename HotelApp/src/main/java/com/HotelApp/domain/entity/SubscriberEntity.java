package com.HotelApp.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscribers")
public class SubscriberEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDateTime timeOfSubscription;

    private Integer counterOfSubscriptions;

    public SubscriberEntity() {}

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
}
