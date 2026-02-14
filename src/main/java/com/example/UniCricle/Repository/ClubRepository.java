package com.example.UniCricle.Repository;

import com.example.UniCricle.model.Club;
import com.example.UniCricle.model.enums.ClubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);

    List<Club> findByApprovedTrue();

    List<Club> findByApprovedFalse();

    List<Club> findByStatus(ClubStatus status);

    @Query("SELECT c FROM Club c WHERE c.status = 'PENDING'")
    List<Club> findPendingClubs();

    @Query("SELECT c FROM Club c WHERE c.leader.id = :leaderId")
    List<Club> findClubsManagedBy(@Param("leaderId") Long leaderId);

    List<Club> findByLeaderId(Long leaderId);

    @Query("SELECT c FROM Club c WHERE c.president.id = :userId OR c.vicePresident.id = :userId")
    List<Club> findClubsByPresidentOrVicePresident(@Param("userId") Long userId);
}