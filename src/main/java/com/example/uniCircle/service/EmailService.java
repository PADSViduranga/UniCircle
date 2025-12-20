package com.example.uniCircle.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
@Service
public class EmailService {
    private static final Logger logger= LoggerFactory.getLogger(
            EmailService.class);

    public void sendVerificationEmail(String email, String token){
        try {
            String verificationLink= "http://localhost:8080/api/auth/verify-email?token=" + token;

            logger.info("-----Email verification-----");
            logger.info("To: {}",email);
            logger.info("subject: verify your uniCircle account");
            logger.info("verification Link: {}",verificationLink);
            logger.info("-----------------------");
        }
        catch (Exception e) {
            logger.error("Failed to send verification email to: {}", email, e);
            throw new RuntimeException("Failed to send verification email", e);
        }

    }
}
