package com.HotelApp.domain.models.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.HotelApp.common.constants.ValidationConstants.*;

public class ChangeUserPasswordBindingModel {

    @NotBlank(message = EMPTY_PASSWORD)
    @Size(min = 5, message = PASSWORD_LENGTH)
    private String oldPassword;

    @NotBlank(message = EMPTY_PASSWORD)
    @Size(min = 5, message = PASSWORD_LENGTH)
    private String newPassword;

    private String confirmNewPassword;

    public ChangeUserPasswordBindingModel() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public ChangeUserPasswordBindingModel setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangeUserPasswordBindingModel setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public ChangeUserPasswordBindingModel setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        return this;
    }
}
