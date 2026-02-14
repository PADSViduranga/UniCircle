package com.example.UniCricle.controller;

import com.example.UniCricle.Service.CharityService;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/charity")
public class CharityController {

    private final CharityService charityService;

    // GET all charities
    @GetMapping
    public String viewCharities(Model model) {
        model.addAttribute("charities", charityService.getAllCharities());
        return "charity"; // match your HTML file
    }

    // POST participate in a charity
    @PostMapping("/join/{id}")
    public String joinCharity(@PathVariable Long id,
                              @AuthenticationPrincipal User user) {
        charityService.participate(user, id);
        return "redirect:/charity";
    }
}