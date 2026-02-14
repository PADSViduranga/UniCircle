package com.example.UniCricle.Service;

import com.example.UniCricle.Repository.EventRepository;
import com.example.UniCricle.Repository.UserRepository;
import com.example.UniCricle.model.Event;
import com.example.UniCricle.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public Event createEvent(String title, String description, String location, String eventDate) {
        Event event = Event.builder()
                .title(title)
                .description(description)
                .location(location)
                .eventDate(eventDate)
                .approved(false) // Default to pending
                .build();

        return eventRepository.save(event);
    }

    public void attendEvent(User user) {
        user.setPoints(user.getPoints() + 6);
        userRepository.save(user);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getPendingEvents() {
        return eventRepository.findByApproved(false);
    }

    public void approveEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setApproved(true);
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Event createEvent(Event event) {
        event.setApproved(false); // Admin approval required
        return eventRepository.save(event);
    }
}