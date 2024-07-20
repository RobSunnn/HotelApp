package com.HotelApp.service.impl;

import com.HotelApp.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final String hotelEmail;

    public MailServiceImpl(
            TemplateEngine templateEngine,
            JavaMailSender javaMailSender,
            @Value("${mail.hotel}") String hotelEmail
    ) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
        this.hotelEmail = hotelEmail;
    }

    @Override
    @Async
    public void sendBonusVoucherEmail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            ClassPathResource imageResource = new ClassPathResource("static/images/voucher.jpeg");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(hotelEmail);
            mimeMessageHelper.setReplyTo(hotelEmail);
            mimeMessageHelper.setSubject("Thank you for subscribing!");
            mimeMessageHelper.setText(generateBonusVoucherBody(), true);

            mimeMessageHelper.addInline("voucher", imageResource, "image/jpeg");
            mimeMessageHelper.addAttachment("voucher.jpeg", imageResource);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException | MailSendException ignored) {
            log.warn("Please connect the smtp server to proceed sending a bonus voucher to client.");
        }
    }

    @Override
    @Async
    public void sendConfirmationEmailForOnlineReservation(String userEmail, String userFullName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            ClassPathResource imageResource = new ClassPathResource("static/images/success.jpg");

            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setFrom(hotelEmail);
            mimeMessageHelper.setReplyTo(hotelEmail);
            mimeMessageHelper.setSubject("Thank you for your reservation!");
            mimeMessageHelper.setText(generateOnlineReservationConfirmBody(userFullName), true);

            mimeMessageHelper.addInline("success", imageResource, "image/jpg");

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException | MailSendException ignored) {
            log.warn("Please connect the smtp server to proceed sending confirmation email.");
        }
    }

    private String generateBonusVoucherBody() {
        Context context = new Context();
        context.setVariable("voucherCid", "cid:voucher");

        return templateEngine.process("email/subscriber-bonus", context);
    }

    private String generateOnlineReservationConfirmBody(String fullName) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("successCid", "cid:success");

        return templateEngine.process("email/online-reservation-confirm", context);
    }

}
