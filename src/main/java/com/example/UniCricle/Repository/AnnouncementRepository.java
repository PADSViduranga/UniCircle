package com.example.UniCricle.Repository;

import com.example.UniCricle.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByClubIdAndStatusOrderByCreatedAtDesc(Long clubId, String status);

    List<Announcement> findByStatusOrderByCreatedAtDesc(String status);
}
