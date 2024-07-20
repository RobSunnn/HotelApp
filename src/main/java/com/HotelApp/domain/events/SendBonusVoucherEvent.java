package com.HotelApp.domain.events;

import org.springframework.context.ApplicationEvent;

public class SendBonusVoucherEvent extends ApplicationEvent {

    private final String email;

    public SendBonusVoucherEvent(Object source, String email) {
        super(source);
        this.email = email;

    }

    public String getEmail() {
        return email;
    }


}
