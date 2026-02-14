package com.example.UniCricle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location; // Added to match your HTML 'venue'

    @Column(nullable = false)
    private String eventDate; // Added to match your HTML 'date' (String for simple form input)

    @Column(nullable = false)
    private boolean approved = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Relationships
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Helper method to initialize default values when using the builder
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}