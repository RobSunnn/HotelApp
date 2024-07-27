package com.HotelApp.service;

public interface MailService {

    void sendBonusVoucherEmail(String email);

    void sendConfirmationEmailForOnlineReservation(String userEmail, String userFullName);
}
