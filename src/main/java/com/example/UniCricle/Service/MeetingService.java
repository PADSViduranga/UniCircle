package com.example.UniCricle.Service;

import com.example.UniCricle.Repository.MeetingRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.Meeting;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    public Meeting createMeeting(String topic, String description) {
        Meeting meeting = Meeting.builder()
                .topic(topic)
                .description(description)
                .build();

        return meetingRepository.save(meeting);
    }

    public void attendMeeting(User user) {
        user.setPoints(user.getPoints() + 4);
        userRepository.save(user);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
}
