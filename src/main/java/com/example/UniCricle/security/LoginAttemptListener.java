package com.example.UniCricle.security;

import com.example.UniCricle.Auth.UserAccountService;
import com.example.UniCricle.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAttemptListener {
    private final UserRepository userRepository;
    private final UserAccountService userAccountService;

    @EventListener
    public void onLoginFailure(AuthenticationFailureBadCredentialsEvent event){
        String username = event.getAuthentication().getName();
        userRepository.findByUsername(username)
                .ifPresent(userAccountService::processFiledLogin);
    }
    @EventListener
    public void onLoginSuccess(AuthenticationSuccessEvent event){
        String username = event.getAuthentication().getName();
        userRepository.findByUsername(username)
                .ifPresent(userAccountService::resetAttempts)
        ;
    }
}
