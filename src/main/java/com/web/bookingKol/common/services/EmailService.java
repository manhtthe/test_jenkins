package com.web.bookingKol.common.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String code) {
        String subject = "Verify your email - BookingKOL";
        String verifyUrl = "http://localhost:8080/auth/verify?email=" + toEmail + "&code=" + code;
        String content = "Xin chào!\n\nVui lòng click link dưới đây để xác thực tài khoản của bạn:\n"
                + verifyUrl + "\n\nLink có hiệu lực trong 24h.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}

