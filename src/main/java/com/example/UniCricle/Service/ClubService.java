package com.example.UniCricle.Service;

import com.example.UniCricle.Repository.ClubMemberRepository;
import com.example.UniCricle.Repository.ClubRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.*;
import com.example.UniCricle.model.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;

    // --- 1. ADMIN FEATURE: CREATE & ASSIGN ---
    @Transactional
    public void createClubByAdmin(String name, String description, Long presId, Long vpId, User admin) {
        Club club = Club.builder()
                .name(name).description(description).createdBy(admin)
                .approved(true).status(ClubStatus.APPROVED).createdAt(LocalDateTime.now())
                .president(presId != null ? userRepository.findById(presId).orElse(null) : null)
                .vicePresident(vpId != null ? userRepository.findById(vpId).orElse(null) : null)
                .build();

        Club savedClub = clubRepository.save(club);

        // Renamed and changed visibility to public
        if (presId != null) assignRoleToUser(presId, savedClub, ClubRole.PRESIDENT);
        if (vpId != null) assignRoleToUser(vpId, savedClub, ClubRole.VICE_PRESIDENT);
    }

    // UPDATED: Public visibility so AdminController can use it
    @Transactional
    public void assignRoleToUser(Long userId, Club club, ClubRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ClubMember member = clubMemberRepository.findByUserAndClub(user, club)
                .orElse(ClubMember.builder().user(user).club(club).build());

        member.setRole(role);
        member.setApproved(true);
        member.setStatus(MembershipStatus.APPROVED);
        clubMemberRepository.save(member);
    }

    // --- 2. ADMIN FEATURE: APPROVE PENDING REQUEST ---
    @Transactional
    public void approveClubRequest(Long clubId, Long presId, Long vpId) {
        Club club = clubRepository.findById(clubId).orElseThrow();
        club.setStatus(ClubStatus.APPROVED);
        club.setApproved(true);
        if (presId != null) {
            club.setPresident(userRepository.findById(presId).orElse(null));
            assignRoleToUser(presId, club, ClubRole.PRESIDENT);
        }
        clubRepository.save(club);
    }

    // --- REST OF YOUR METHODS (Unchanged) ---
    @Transactional
    public void createClubRequest(String name, String description, User creator) {
        Club club = Club.builder().name(name).description(description).creator(creator)
                .approved(false).status(ClubStatus.PENDING).createdAt(LocalDateTime.now()).build();
        clubRepository.save(club);
    }

    @Transactional
    public ClubMember enrollUserToClub(User user, Club club) {
        if (clubMemberRepository.findByUserAndClub(user, club).isPresent()) {
            throw new RuntimeException("Already enrolled or request pending");
        }
        return clubMemberRepository.save(ClubMember.builder()
                .user(user).club(club).role(ClubRole.MEMBER)
                .approved(false).status(MembershipStatus.PENDING).build());
    }

    @Transactional
    public void approveMemberRequest(Long id) {
        ClubMember m = clubMemberRepository.findById(id).orElseThrow();
        m.setApproved(true);
        m.setStatus(MembershipStatus.APPROVED);
        User u = m.getUser();
        u.setPoints(u.getPoints() + 10);
        userRepository.save(u);
        clubMemberRepository.save(m);
    }

    @Transactional
    public void rejectMemberRequest(Long id) {
        ClubMember m = clubMemberRepository.findById(id).orElseThrow();
        m.setStatus(MembershipStatus.REJECTED);
        clubMemberRepository.save(m);
    }

    public List<Club> getClubsManagedBy(Long userId) {
        return clubRepository.findClubsByPresidentOrVicePresident(userId);
    }
    public Club getClubById(Long id) { return clubRepository.findById(id).orElseThrow(); }
    public List<Club> getAllClubs() { return clubRepository.findAll(); }
    public List<Club> getAllClubsForUsers() { return clubRepository.findByApprovedTrue(); }
    @Transactional
    public void deleteClub(Long id) { clubRepository.deleteById(id); }
}