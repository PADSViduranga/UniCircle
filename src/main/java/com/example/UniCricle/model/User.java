package com.example.UniCricle.model;

import com.example.UniCricle.model.enums.ClubRole;
import com.example.UniCricle.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ClubRole clubRole;

    @Column(nullable = false)
    private int failedAttempts;

    @Column(nullable = false)
    private boolean accountLocked;

    private LocalDateTime lockTime;

    private int points = 0;
}
