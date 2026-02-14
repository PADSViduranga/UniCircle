package com.example.UniCricle.controller;

import com.example.UniCricle.Service.AnnouncementService;
import com.example.UniCricle.model.Announcement;
import com.example.UniCricle.Repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final ClubRepository clubRepository;

    // 1. User View: View all approved announcements for a club
    @GetMapping("/club/{clubId}")
    public String viewAnnouncements(@PathVariable Long clubId, Model model) {
        List<Announcement> announcements = announcementService.getApprovedAnnouncements(clubId);

        model.addAttribute("announcements", announcements);
        model.addAttribute("clubId", clubId); // Needed for the "Create Post" button link

        // Pass club name if it exists, otherwise a default string
        String clubName = clubRepository.findById(clubId)
                .map(club -> club.getName())
                .orElse("Club");
        model.addAttribute("clubName", clubName);

        // Path matches your file: templates/announcement.html
        return "announcement";
    }

    // 2. Create logic remains the same, but redirected to dashboard
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLUB_LEADER')")
    public String createAnnouncement(@ModelAttribute Announcement announcement,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {

        announcementService.createAnnouncement(announcement, auth);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            redirectAttributes.addFlashAttribute("success", "Announcement published successfully!");
        } else {
            redirectAttributes.addFlashAttribute("info", "Submitted! Waiting for Admin approval.");
        }

        return "redirect:/dashboard";
    }

    // 3. Admin View: List all pending requests
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewPendingRequests(Model model) {
        model.addAttribute("pendingAnnouncements", announcementService.getPendingAnnouncements());
        return "admin/pending_announcements";
    }

    // 4. Admin Action: Approve
    @PostMapping("/admin/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveAnnouncement(@PathVariable Long id) {
        announcementService.approveAnnouncement(id);
        return "redirect:/announcements/admin/pending";
    }
}