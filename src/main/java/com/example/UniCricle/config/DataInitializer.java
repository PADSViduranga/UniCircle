package com.example.UniCricle.config;

import com.example.UniCricle.Repository.ClubMemberRepository;
import com.example.UniCricle.Repository.ClubRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.Club;
import com.example.UniCricle.model.ClubMember;
import com.example.UniCricle.model.User;
import com.example.UniCricle.model.enums.ClubRole;
import com.example.UniCricle.model.enums.ClubStatus;
import com.example.UniCricle.model.enums.MembershipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.UniCricle.model.enums.Role;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // ✅ Create test user as club leader
        User leader = userRepository.findByUsername("leader1").orElse(null);
        if (leader == null) {
            leader = User.builder()
                    .username("leader1")
                    .email("leader@example.nsbm.ac.lk")
                    .password(passwordEncoder.encode("Pw1234"))
                    .role(Role.USER) // Keep USER role; leader info is in ClubMember
                    .points(0)
                    .build();
            userRepository.save(leader);
        }

        // ✅ Create test club
        Club club = clubRepository.findByName("Test Club").orElse(null);
        if (club == null) {
            club = Club.builder()
                    .name("Test Club")
                    .description("This is a test club")
                    .createdBy(leader)
                    .status(ClubStatus.APPROVED)
                    .approved(true)
                    .createdAt(LocalDateTime.now())
                    .build();
            clubRepository.save(club);
        }

        // ✅ Assign leader as PRESIDENT in ClubMember
        ClubMember member = clubMemberRepository.findByUserAndClub(leader, club).orElse(null);
        if (member == null) {
            member = ClubMember.builder()
                    .user(leader)
                    .club(club)
                    .role(ClubRole.PRESIDENT) // 🔹 important: sets leader role
                    .status(MembershipStatus.APPROVED)
                    .approved(true)
                    .build();
            clubMemberRepository.save(member);
        }

        System.out.println("✅ Test club and leader initialized!");
    }
}
