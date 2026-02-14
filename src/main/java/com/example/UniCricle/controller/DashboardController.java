package com.example.UniCricle.controller;
import com.example.UniCricle.Service.ClubService;
import com.example.UniCricle.Service.EventService;
import com.example.UniCricle.Service.MeetingService;
import com.example.UniCricle.Service.CharityService;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final ClubService clubService;
    private final EventService eventService;
    private final MeetingService meetingService;
    private final CharityService charityService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole().name());
        model.addAttribute("points", user.getPoints());

        // This method is now correctly defined in ClubService
        model.addAttribute("clubCount", clubService.getAllClubs().size());
        model.addAttribute("eventCount", eventService.getAllEvents().size());
        model.addAttribute("meetingCount", meetingService.getAllMeetings().size());
        model.addAttribute("charityCount", charityService.getAllCharities().size());

        return "dashboard";
    }
}