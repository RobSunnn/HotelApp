package com.HotelApp.domain.models.binding;

import com.HotelApp.validation.annotation.ValidEmail;
import jakarta.validation.constraints.NotBlank;

import static com.HotelApp.common.constants.ValidationConstants.EMAIL_NOT_BLANK;
import static com.HotelApp.common.constants.ValidationConstants.INVALID_EMAIL;

public class AddSubscriberBindingModel {

    @ValidEmail(message = INVALID_EMAIL)
    private String subscriberEmail;

    public AddSubscriberBindingModel() {
    }

    public String getSubscriberEmail() {
        return subscriberEmail;
    }

    public AddSubscriberBindingModel setSubscriberEmail(String subscriberEmail) {
        this.subscriberEmail = subscriberEmail;
        return this;
    }
}
