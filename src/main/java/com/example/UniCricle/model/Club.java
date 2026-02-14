package com.example.UniCricle.model;

import com.example.UniCricle.model.enums.ClubStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "club")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader; // The President who manages the club

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator; // The person who requested the club

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ClubMember> members = new HashSet<>();

    @Column(nullable = false)
    private boolean approved = false;

    @Enumerated(EnumType.STRING)
    private ClubStatus status; // PENDING, APPROVED, REJECTED

    @Transient
    private long approvedMemberCount;

    @Transient
    private int memberCount;

    @ManyToOne
    @JoinColumn(name = "president_id")
    private User president; // Assigned by Admin

    @ManyToOne
    @JoinColumn(name = "vice_president_id")
    private User vicePresident; // Assigned by Admin

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;
}