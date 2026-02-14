package com.example.UniCricle.controller;

import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.Service.MeetingService;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final UserRepository userRepository;

    @GetMapping
    public String meetings(Model model) {
        // Use plural name "meetings" to avoid confusion
        model.addAttribute("meetings", meetingService.getAllMeetings());
        return "meeting"; // Make sure meeting.html exists in templates/
    }

    @PostMapping("/create")
    public String createMeeting(@RequestParam String topic,
                                @RequestParam String description) {
        meetingService.createMeeting(topic, description);
        return "redirect:/meetings";
    }

    @PostMapping("/attend")
    public String attendMeeting(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();
        meetingService.attendMeeting(user);
        return "redirect:/meetings";
    }
}
