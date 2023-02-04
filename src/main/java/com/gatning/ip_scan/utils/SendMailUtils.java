package com.gatning.ip_scan.utils;

import com.gatning.ip_scan.entity.SimpleEmailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMailUtils {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(SimpleEmailEntity simpleEmailEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(simpleEmailEntity.getSubject());
        message.setText(simpleEmailEntity.getContent());
        message.setTo(simpleEmailEntity.getTos());
        mailSender.send(message);
    }
}
