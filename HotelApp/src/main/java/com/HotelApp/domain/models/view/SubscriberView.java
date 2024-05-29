package com.HotelApp.domain.models.view;

import java.time.LocalDateTime;

public class SubscriberView {

    private Long id;
    private String email;

    private LocalDateTime timeOfSubscription;

    private Integer counterOfSubscriptions;

    public SubscriberView() {}

    public Long getId() {
        return id;
    }

    public SubscriberView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SubscriberView setEmail(String email) {
        this.email = email;
        return this;
    }

    public LocalDateTime getTimeOfSubscription() {
        return timeOfSubscription;
    }

    public SubscriberView setTimeOfSubscription(LocalDateTime timeOfSubscription) {
        this.timeOfSubscription = timeOfSubscription;
        return this;
    }

    public Integer getCounterOfSubscriptions() {
        return counterOfSubscriptions;
    }

    public SubscriberView setCounterOfSubscriptions(Integer counterOfSubscriptions) {
        this.counterOfSubscriptions = counterOfSubscriptions;
        return this;
    }
}
