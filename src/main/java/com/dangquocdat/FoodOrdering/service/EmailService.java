package com.dangquocdat.FoodOrdering.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendVerificationEmail(String to, String subject, String content) throws MessagingException;
}
