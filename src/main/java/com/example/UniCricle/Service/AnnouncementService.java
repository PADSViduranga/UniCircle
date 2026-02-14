package com.example.UniCricle.Service;

import com.example.UniCricle.Repository.AnnouncementRepository;
import com.example.UniCricle.model.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    // Creating announcement
    public void createAnnouncement(Announcement announcement, Authentication auth) {
        announcement.setCreatedAt(LocalDateTime.now());

        // Check if the user is an Admin
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            announcement.setStatus("APPROVED");
        } else {
            announcement.setStatus("PENDING");
        }

        announcementRepository.save(announcement);
    }

    //   Approving Announcement
    public void approveAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcement.setStatus("APPROVED");
        announcementRepository.save(announcement);
    }

    //  Rejecting announcement by admin
    public void rejectAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcement.setStatus("REJECTED");
        announcementRepository.save(announcement);
    }

    // Getters for the Controller
    public List<Announcement> getApprovedAnnouncements(Long clubId) {
        return announcementRepository.findByClubIdAndStatusOrderByCreatedAtDesc(clubId, "APPROVED");
    }

    public List<Announcement> getPendingAnnouncements() {
        return announcementRepository.findByStatusOrderByCreatedAtDesc("PENDING");
    }
}
