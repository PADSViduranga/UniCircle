package com.example.UniCricle.controller;

import com.example.UniCricle.Repository.ClubMemberRepository;
import com.example.UniCricle.Repository.ClubRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.Service.ClubService;
import com.example.UniCricle.model.*;
import com.example.UniCricle.model.enums.ClubStatus;
import com.example.UniCricle.model.enums.MembershipStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final UserRepository userRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;

    // 1. List only approved clubs
    @GetMapping("/all")
    public String listAllClubs(Model model) {
        model.addAttribute("clubs", clubRepository.findByStatus(ClubStatus.APPROVED));
        return "clubs/list";
    }

    // 2. Main clubs page (Role-based filtering)
    @GetMapping
    public String listClubs(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            if (user.getRole().name().equals("ADMIN")) {
                model.addAttribute("clubs", clubService.getAllClubs());
            } else {
                model.addAttribute("clubs", clubService.getAllClubsForUsers());
            }
        } else {
            model.addAttribute("clubs", clubService.getAllClubsForUsers());
        }
        return "clubs";
    }

    // 3. Create club request (Security check included)
    @PostMapping("/create")
    public String createClub(@RequestParam String name, @RequestParam String description,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User creator = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        String role = creator.getRole().name();
        if (!role.equals("ADMIN") && !role.equals("PRESIDENT")) {
            throw new RuntimeException("Unauthorized: Only Admin or President can create clubs.");
        }
        clubService.createClubRequest(name, description, creator);
        return "redirect:/clubs";
    }

    // 4. Join/Enroll logic
    @PostMapping("/join/{id}")
    public String joinClub(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Club club = clubService.getClubById(id);
        clubService.enrollUserToClub(user, club);
        return "redirect:/clubs/view/" + id;
    }

    // 5. Clubs list with manual member count
    @GetMapping("/clubs")
    public String getClubs(Model model) {
        List<Club> clubs = clubService.getAllClubs();
        clubs.forEach(club -> club.setMemberCount(club.getMembers().size()));
        model.addAttribute("clubs", clubs);
        return "clubs";
    }

    // 6. Detailed Club View (Approval logic)
    @GetMapping("/view/{id}")
    public String viewClub(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Club club = clubService.getClubById(id);
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Optional<ClubMember> membership = clubMemberRepository.findByUserAndClub(user, club);

        model.addAttribute("club", club);
        if (membership.isPresent()) {
            MembershipStatus status = membership.get().getStatus();
            if (status == MembershipStatus.APPROVED) {
                model.addAttribute("isApproved", true);
            } else {
                model.addAttribute("isApproved", false);
                model.addAttribute("pendingMessage", "Wait for the Leader to approve your request.");
            }
        } else {
            model.addAttribute("isApproved", false);
        }
        return "leader/club-register";
    }

    // 7. Legacy ID redirect
    @GetMapping("/{id}")
    public String handleLegacyUrl(@PathVariable Long id) {
        return "redirect:/clubs/view/" + id;
    }
}