package com.example.UniCricle.Repository;

import com.example.UniCricle.model.Club;
import com.example.UniCricle.model.ClubMember;
import com.example.UniCricle.model.User;
import com.example.UniCricle.model.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByUserAndClub(User user, Club club);

    List<ClubMember> findByClubAndApprovedFalse(Club club);

    List<ClubMember> findByUserAndRoleIn(User user, List<com.example.UniCricle.model.enums.ClubRole> roles);

    List<ClubMember> findByClubAndStatus(Club club, MembershipStatus status);

    List<ClubMember> findByClubLeaderAndStatus(User leader, MembershipStatus status);


    List<ClubMember> findByUser(User user);

    Object findByStatus(MembershipStatus status);
}
