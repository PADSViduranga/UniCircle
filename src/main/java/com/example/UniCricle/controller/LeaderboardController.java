package com.example.UniCricle.controller;

import com.example.UniCricle.Service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public String leaderboard(Model model) {
        // Add sorted user list to the model
        model.addAttribute("users", leaderboardService.getLeaderboard());
        // Thymeleaf template must be: leaderboard.html
        return "leaderboard";
    }

}
