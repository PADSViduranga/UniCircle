package com.example.UniCricle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about"; // This must match templates/about.html
    }

    // Optional: Add this if you want to create a contact.html later
    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }
}
