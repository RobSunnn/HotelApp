package com.HotelApp.domain.events;

import org.springframework.context.ApplicationEvent;

public class OnlineReservationEvent extends ApplicationEvent {
    private final String userEmail;
    private final String userFullName;

    public OnlineReservationEvent(Object source, String userEmail, String userFullName) {
        super(source);
        this.userEmail = userEmail;
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }
}
