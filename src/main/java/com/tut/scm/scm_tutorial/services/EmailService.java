package com.tut.scm.scm_tutorial.services;

public interface EmailService {


    void sendEmail(String to,String subject,String body);

    void sendEmailWithHtml();

    void sendEmailWithAttachement();


}
