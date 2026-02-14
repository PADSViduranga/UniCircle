package com.example.UniCricle.Auth;

import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS=3;
    private static final int LOCK_DURATION_HOURS=3;


    public void checkUnlock(User user) {
        if (user.isAccountLocked() && user.getLockTime() != null) {
            if (LocalDateTime.now().isAfter(user.getLockTime())) {
                user.setAccountLocked(false);
                user.setFailedAttempts(0);
                user.setLockTime(null);
                userRepository.save(user);
            }
        }
    }
    public void processFiledLogin(User user){

        int newAttempts=user.getFailedAttempts()+1;
        user.setFailedAttempts(newAttempts);

        if(newAttempts>=MAX_FAILED_ATTEMPTS){
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    public void processFailedLogin(User user) {
        int newAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newAttempts);

        if (newAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setLockTime(LocalDateTime.now());
        }
        userRepository.save(user);
    }

    public boolean isAccountLocked(User user) {
        if (!user.isAccountLocked() ) {
            return false;
        }
        LocalDateTime lockTime = user.getLockTime();
        if (lockTime != null && lockTime.plusHours(3).isBefore(LocalDateTime.now())) {
            user.setAccountLocked(false);
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);

            return false;
        }
        return true;

    }
    public void resetAttempts(User user) {
        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);
        userRepository.save(user);
    }
}
