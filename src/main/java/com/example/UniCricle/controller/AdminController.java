package com.example.UniCricle.controller;

import com.example.UniCricle.Repository.*;
import com.example.UniCricle.Service.ClubService;
import com.example.UniCricle.Service.EventService;
import com.example.UniCricle.model.*;
import com.example.UniCricle.model.enums.ClubRole;
import com.example.UniCricle.model.enums.ClubStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final CharityRepository charityRepository;
    private final EventService eventService;
    private final ClubService clubService;

    // --- 1. DASHBOARD OVERVIEW ---
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("eventCount", eventRepository.count());
        model.addAttribute("clubCount", clubRepository.count());
        model.addAttribute("charityCount", charityRepository.count());
        return "admin/admin-dashboard";
    }

    // --- 2. USER MANAGEMENT ---
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/manage-users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // --- 3. EVENT MANAGEMENT (Restored) ---
    @GetMapping("/events")
    public String manageEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "admin/manage-events";
    }

    @GetMapping("/events/create")
    public String showForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/create-event";
    }

    @PostMapping("/events/approve/{id}")
    public String approveEvent(@PathVariable Long id) {
        eventService.approveEvent(id);
        return "redirect:/admin/events";
    }
    @PostMapping("/events/create")
    public String saveEvent(@ModelAttribute("event") Event event) {
        // Ensure status is approved since an admin is creating it
        event.setApproved(true);



        eventRepository.save(event);
        return "redirect:/admin/events";
    }


    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/events";
    }

    // --- 4. CHARITY MANAGEMENT (Restored) ---
    @GetMapping("/charity")
    public String manageCharity(Model model) {
        model.addAttribute("charities", charityRepository.findAll());
        return "admin/manage-charity";
    }

    @PostMapping("/charity/approve/{id}")
    public String approveCharity(@PathVariable Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow();
        charity.setApproved(true);
        charityRepository.save(charity);
        return "redirect:/admin/charity";
    }
    @GetMapping("/charity/create")
    public String showCreateCharityForm(Model model) {
        model.addAttribute("charity", new Charity()); // This "charity" name must match th:object
        return "admin/create-charity";
    }

    @PostMapping("/charity/create")
    public String saveCharity(@ModelAttribute("charity") Charity charity,
                              @AuthenticationPrincipal UserDetails userDetails) {

        // 1. Get the current logged-in user from the database
        User admin = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Attach that user to the charity campaign
        charity.setCreatedBy(admin);
        charity.setApproved(true);

        // 3. Save to database
        charityRepository.save(charity);

        return "redirect:/admin/charity";
    }



    @PostMapping("/charity/delete/{id}")
    public String deleteCharity(@PathVariable Long id) {
        charityRepository.deleteById(id);
        return "redirect:/admin/charity";
    }

    // --- 5. CLUB MANAGEMENT (UPGRADED WITH ROLE ASSIGNMENT) ---
    @GetMapping("/clubs")
    public String manageClubs(Model model) {
        model.addAttribute("clubs", clubRepository.findAll());
        model.addAttribute("users", userRepository.findAll()); // Required for leader assignment dropdowns
        return "admin/manage-clubs";
    }

    @GetMapping("/clubs/create")
    public String createClubForm(Model model) {
        model.addAttribute("club", new Club());
        model.addAttribute("users", userRepository.findAll());
        return "admin/create-club";
    }

    @PostMapping("/clubs/create")
    public String createClubSubmit(@ModelAttribute Club club,
                                   @RequestParam Long presidentId,
                                   @RequestParam Long vicePresidentId,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        User admin = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        clubService.createClubByAdmin(club.getName(), club.getDescription(), presidentId, vicePresidentId, admin);
        return "redirect:/admin/clubs";
    }

    @PostMapping("/clubs/approve/{id}")
    public String approveClub(@PathVariable Long id, @RequestParam Long presidentId) {
        // Approves a student's club request and assigns the Leader simultaneously
        clubService.approveClubRequest(id, presidentId, null);
        return "redirect:/admin/clubs";
    }

    @PostMapping("/clubs/delete/{id}") // Removed the extra /admin/
    public String deleteClub(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clubService.deleteClub(id);
            redirectAttributes.addFlashAttribute("success", "Club deleted successfully!");
        } catch (Exception e) {
            // Log the error and notify the user, but still redirect
            redirectAttributes.addFlashAttribute("error", "Could not delete club: " + e.getMessage());
        }

        // Redirect back to the correct list page (/admin/clubs)
        return "redirect:/admin/clubs";
    }
    @PostMapping("/clubs/assign-leaders/{id}")
    public String assignLeaders(@PathVariable Long id,
                                @RequestParam(required = false) Long presidentId,
                                @RequestParam(required = false) Long vicePresidentId) {

        Club club = clubRepository.findById(id).orElseThrow();

        if (presidentId != null) {
            clubService.assignRoleToUser(presidentId, club, ClubRole.PRESIDENT);
            club.setPresident(userRepository.findById(presidentId).orElse(null));
        }

        if (vicePresidentId != null) {
            clubService.assignRoleToUser(vicePresidentId, club, ClubRole.VICE_PRESIDENT);
            club.setVicePresident(userRepository.findById(vicePresidentId).orElse(null));
        }

        clubRepository.save(club);
        return "redirect:/admin/clubs";
    }
}