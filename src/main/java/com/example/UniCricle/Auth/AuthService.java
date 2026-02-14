package com.example.UniCricle.Auth;

import com.example.UniCricle.DTO.RegisterRequest;
import com.example.UniCricle.model.User;
import com.example.UniCricle.model.enums.Role;
import com.example.UniCricle.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        if (!request.getEmail().endsWith("@example.nsbm.ac.lk")) {
            throw new RuntimeException("Invalid NSBM email");
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .points(0)
                .failedAttempts(0)
                .accountLocked(false)
                .build();

        userRepository.save(user);
    }
}
