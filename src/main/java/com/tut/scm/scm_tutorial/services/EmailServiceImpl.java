package com.tut.scm.scm_tutorial.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {
        
        SimpleMailMessage message=new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);//domain name
        javaMailSender.send(message);
        System.out.println("Email Sent successfully...");

    }

   @Override
    public void sendEmailWithAttachement() {
        throw new UnsupportedOperationException("");
        
    }

    @Override
    public void sendEmailWithHtml() {
        
        
    }



}
