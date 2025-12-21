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
    public void sendPasswordResetEmail(String email, String resetToken){
        try {
            String resetLink="http://localhost:8080/api/auth/reset-password?token="+resetToken;

            logger.info("----password reset email----");
            logger.info("To: {}",email);
            logger.info("subject: password reset email request");
            logger.info("Reset Link: {}",resetLink);
            logger.info("----------------------");
        }
        catch (Exception e){
            logger.error("Failed to send password reset email to: {}",email,e);
            throw new RuntimeException("failed to send password set email",e);
        }
    }
    public void sendWelcomeEmail(String email,String fullName){
        try {
            logger.info("---Welcome Email");
            logger.info("To : {}",email);
            logger.info("subject: welcome to Unicircle");
            logger.info("hello {}, Welcome to uni circle",email);
            logger.info("---------------------------");
        }
        catch (Exception e){
            logger.error("welcom email sending failed to: {}",email,e);
            throw new RuntimeException("Failed ");
        }
    }

    public void sendAccountLockedEmail(String email,String reason){
        try {
            logger.info("----Account locked email----");
            logger.info("To: {}",email);
            logger.info("subject: your account has been locked");
            logger.info("Reason: {}",reason);
            logger.info("contact your admins to unlock your account");
            logger.info("------------------------");
        }
        catch (Exception e){
            logger.error("failed to send account locked email to: ",email,e);
            throw new RuntimeException("failed to send account locked email",e);
        }
    }

    public void sendRoleChangeEmail(String email,String currentRole,String newRole){
        try {
            logger.info("----Role changing info----");
            logger.info("To: {}",email);
            logger.info("subject: your role has been changed");
            logger.info("your role has benn changed from {} to {}",currentRole,newRole);
            logger.info("--------------------------------");
        }
        catch (Exception e){
            logger.info("failed to send role change email to: ",email,e);
            throw new RuntimeException("failed to send role changed email",e);
        }
    }
}
