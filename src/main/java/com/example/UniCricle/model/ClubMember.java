package com.example.UniCricle.model;

import com.example.UniCricle.model.enums.ClubRole;
import com.example.UniCricle.model.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "club_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Club club;

    @Enumerated(EnumType.STRING)
    private ClubRole role; // PRESIDENT, VICE_PRESIDENT, MEMBER

    @Column(nullable = false)
    private boolean approved = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;
}
