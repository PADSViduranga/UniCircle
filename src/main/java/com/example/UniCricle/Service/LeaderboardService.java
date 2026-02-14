package com.example.UniCricle.Service;


import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    // Get all users sorted by points in descending order
    public List<User> getLeaderboard() {
        return userRepository.findAllByOrderByPointsDesc();
    }
}
