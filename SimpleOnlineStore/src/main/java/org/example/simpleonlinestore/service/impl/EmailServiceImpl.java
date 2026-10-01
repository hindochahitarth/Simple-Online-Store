package org.example.simpleonlinestore.service.impl;

import org.example.simpleonlinestore.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Simple Store Login");
        message.setText("Your OTP is: " + otp + "\nThis OTP is valid for 2 minutes.");

        mailSender.send(message);
        System.out.println("OTP sent to email: " + toEmail);
    }
    @Override
    public void sendNotification(String toEmail, String mssg) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Order Details ");
        message.setText("Your Order details "+mssg);
        mailSender.send(message);
    }
}
