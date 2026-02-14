package com.example.UniCricle.Repository;


import com.example.UniCricle.model.Charity;
import com.example.UniCricle.model.CharityParticipant;
import com.example.UniCricle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharityParticipantRepository
        extends JpaRepository<CharityParticipant, Long> {

    Optional<CharityParticipant> findByUserAndCharity(User user, Charity charity);
}
