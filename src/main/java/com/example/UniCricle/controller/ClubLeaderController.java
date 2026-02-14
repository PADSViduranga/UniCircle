package com.example.UniCricle.controller;

import com.example.UniCricle.Repository.ClubMemberRepository;
import com.example.UniCricle.Repository.ClubRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.Service.ClubService;
import com.example.UniCricle.model.*;
import com.example.UniCricle.model.enums.MembershipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/leader")
@RequiredArgsConstructor
public class ClubLeaderController {

    private final ClubService clubService;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    // --- 1. FEATURE: CLUB SELECTOR (The Hub) ---
    // Shows clubs where the user is either President or Vice President
    @GetMapping("/dashboard")
    public String showSelector(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the synchronized service method that checks both President and VP roles
        List<Club> managedClubs = clubService.getClubsManagedBy(user.getId());

        // Shows requests created by this student (to see if Admin approved them yet)
        List<Club> myRequests = clubRepository.findAll().stream()
                .filter(c -> c.getCreator() != null && c.getCreator().getId().equals(user.getId()))
                .toList();

        model.addAttribute("managedClubs", managedClubs);
        model.addAttribute("myRequests", myRequests);
        model.addAttribute("leaderName", user.getUsername());

        return "leader/club-selector";
    }

    // --- 2. FEATURE: SPECIFIC CLUB MANAGEMENT ---
    // The detailed page for a specific club's leaders
    @GetMapping("/dashboard/{clubId}")
    public String leaderDashboard(@PathVariable("clubId") Long clubId, Model model) {
        Club club = clubService.getClubById(clubId);

        model.addAttribute("club", club);

        // Fetches pending join requests for the management list
        model.addAttribute("pendingRequests",
                clubMemberRepository.findByClubAndStatus(club, MembershipStatus.PENDING));

        // Count for the dashboard badge
        model.addAttribute("approvedCount",
                clubMemberRepository.findByClubAndStatus(club, MembershipStatus.APPROVED).size());

        return "leader/leader-dashboard";
    }

    // --- 3. FEATURE: MEMBER APPROVAL (+10 Points Logic) ---
    @PostMapping("/approve/{id}")
    public String approveMember(@PathVariable Long id) {
        ClubMember membership = clubMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        // Calls service which handles point assignment and status update
        clubService.approveMemberRequest(id);

        return "redirect:/leader/dashboard/" + membership.getClub().getId();
    }

    // --- 4. FEATURE: REQUEST NEW CLUB ---
    // For students to request a new club from the leader dashboard
    @PostMapping("/create-club")
    public String submitRequest(@RequestParam String name,
                                @RequestParam String description,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User creator = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calls service to create a PENDING request
        clubService.createClubRequest(name, description, creator);

        return "redirect:/leader/dashboard";
    }

    // --- 5. FEATURE: REJECT MEMBER REQUEST ---
    @PostMapping("/reject-member/{id}")
    public String rejectMember(@PathVariable Long id) {
        ClubMember membership = clubMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        Long clubId = membership.getClub().getId();

        // Sets status to REJECTED in the database
        clubService.rejectMemberRequest(id);

        return "redirect:/leader/dashboard/" + clubId;
    }
}