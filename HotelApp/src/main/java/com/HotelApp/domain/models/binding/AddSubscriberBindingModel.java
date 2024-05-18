package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AddSubscriberBindingModel {

    @NotBlank(message = "Field should not be empty.")
    @Email(message = "Enter valid email.")
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
