package com.example.UniCricle.Service;

import com.example.UniCricle.Repository.CharityParticipantRepository;
import com.example.UniCricle.Repository.CharityRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.Charity;
import com.example.UniCricle.model.CharityParticipant;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharityService {

    private final CharityRepository charityRepository;
    private final CharityParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public Charity createCharity(String title, String description, int points) {
        Charity charity = Charity.builder()
                .title(title)
                .description(description)
                .points(points)
                .build();

        return charityRepository.save(charity);
    }

    public List<Charity> getAllCharities() {
        return charityRepository.findAll();
    }

    public void participate(User user, Long charityId) {
        Charity charity = charityRepository.findById(charityId)
                .orElseThrow(() -> new RuntimeException("Charity not found"));

        if (participantRepository.findByUserAndCharity(user, charity).isPresent()) {
            throw new RuntimeException("Already participated");
        }

        CharityParticipant participant = CharityParticipant.builder()
                .user(user)
                .charity(charity)
                .build();

        // add points
        user.setPoints(user.getPoints() + charity.getPoints());
        userRepository.save(user);

        participantRepository.save(participant);
    }
}
