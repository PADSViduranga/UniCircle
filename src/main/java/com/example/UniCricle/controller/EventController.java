package com.example.UniCricle.controller;

import com.example.UniCricle.Repository.EventRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.Service.EventService;
import com.example.UniCricle.model.Event;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @GetMapping
    public String events(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute Event event) {
        // This will automatically map title, description, location, and eventDate
        eventService.createEvent(event);
        return "redirect:/events";
    }

    @PostMapping("/attend")
    public String attendEvent(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();

        eventService.attendEvent(user);
        return "redirect:/events";
    }

    public List<Event> getPendingEvents() {
        return eventRepository.findByApproved(false);
    }

    public void approveEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setApproved(true);
        eventRepository.save(event);
    }
}
