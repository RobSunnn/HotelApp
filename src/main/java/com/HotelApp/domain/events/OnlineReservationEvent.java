package com.HotelApp.domain.events;

import com.HotelApp.domain.entity.UserEntity;
import org.springframework.context.ApplicationEvent;

public class OnlineReservationEvent extends ApplicationEvent {


    private final UserEntity user;

    public OnlineReservationEvent(Object source, UserEntity user) {
        super(source);
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
