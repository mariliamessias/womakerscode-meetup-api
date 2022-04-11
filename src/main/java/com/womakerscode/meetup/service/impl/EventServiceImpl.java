package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository repository;

    @Override
    public Event save(EventRequest request) {
        return null;
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Event update(EventRequest request, Long id) {
        return null;
    }
}
