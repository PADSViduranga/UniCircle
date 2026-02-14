package com.example.UniCricle;

import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.User;
import com.example.UniCricle.model.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UniCricleApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniCricleApplication.class, args);


	}
    @Bean
    CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin2").isEmpty()) {
                User user = User.builder()
                        .username("admin2")
                        .email("admin2@example.nsbm.ac.lk")
                        .password(passwordEncoder.encode("Ad1234"))
                        .role(Role.ADMIN)        // USER role
                        .points(0)
                        .failedAttempts(0)
                        .accountLocked(false)
                        .build();

                userRepository.save(user);
                System.out.println("✅ Test user created: username=testuser, password=Test1234");
            }

        };
    }

}
