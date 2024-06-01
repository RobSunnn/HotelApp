package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddSubscriberBindingModel {

    @NotBlank(message = "Field should not be empty.")
    @Email(message = "Enter valid email.")
    @Size(max = 100, message = "Your email is too long.")
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
