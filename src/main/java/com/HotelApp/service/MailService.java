package com.HotelApp.service;

import com.HotelApp.domain.entity.UserEntity;

public interface MailService {

    void sendBonusVoucherEmail(String email);

    void sendConfirmationEmailForOnlineReservation(String userEmail, String userFullName);
}
