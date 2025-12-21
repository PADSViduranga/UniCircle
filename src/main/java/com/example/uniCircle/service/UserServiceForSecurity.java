package com.example.uniCircle.service;

import com.example.uniCircle.UserRepository.UserRepository;
import com.example.uniCircle.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceForSecurity {
    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;
    private final EmailService emailService;

    public UserServiceForSecurity(UserRepository userRepository,
                                  PasswordValidator passwordValidator,
                                  EmailService emailService){
        this.userRepository=userRepository;
        this.passwordValidator=passwordValidator;
        this.emailService=emailService;
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("user not found with id: "+id));
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new
                IllegalArgumentException("user not found on email: " +email));
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public List<User> getUserByRole(User.Role role){
        return userRepository.findByRole(role);
    }
    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }
    @Transactional
    public User updateUserProfile(Long id,String fullName,String bio){
        User user=getUserById(id);

        if(fullName !=null && !fullName.trim().isEmpty()){
            user.setFullName(fullName.trim());
        }
        if(bio !=null){
            user.setBio(bio);
        }
        user.setUpdatedBy("self update");
        return userRepository.save(user);
    }

    @Transactional
    public boolean verifyEmail(String token){
        User user=userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        boolean verified=user.verifyEmail(token);
        if(verified){
            userRepository.save(user);
            emailService.sendWelcomeEmail(user.getEmail(),user.getFullName());
        }
        return verified;
    }
    @Transactional
    public void recordFailedLoginAttempts(String email){
        User user=userRepository.findByEmail(email).orElse(null);
        if(user !=null){
            user.recordFailedLoginAttempt();
            userRepository.save(user);

            if(!user.isAccountNonLocked()) {
                emailService.sendAccountLockedEmail(email, "too many attempts");
            }
        }
    }
    @Transactional
    public void recordSuccessfulLogin(String email){
        User user=userRepository.findByEmail(email).orElse(null);
        if(user !=null){
            user.recordSuccessfulLogin();
            userRepository.save(user);
        }
    }
    public User updateUserRole(Long id, User.Role newRole,String updatedBy){
        User user=getUserById(id);

        User.Role currentRole=user.getRole();
        user.setRole(newRole);
        user.setUpdatedBy(updatedBy);

        User savedUser=userRepository.save(user);
        emailService.sendRoleChangeEmail(user.getEmail(), currentRole.name(),
                newRole.name());

        return savedUser;
    }
    @Transactional
    public User setUserEnableStatus(Long id,boolean enabled,String updatedBy){
        User user=getUserById(id);
        user.setEnabled(enabled);
        user.setUpdatedBy(updatedBy);

        return userRepository.save(user);
    }
    @Transactional
    public User unlockedUser(Long id,String updatedBy){
        User user=getUserById(id);
        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        user.setUpdatedBy(updatedBy);

        return userRepository.save(user);
    }

}
