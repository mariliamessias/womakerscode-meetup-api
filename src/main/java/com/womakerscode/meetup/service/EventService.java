package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Status;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(EventRequest request);

    Optional<Event> getEventById(Long id);

    void delete(Long id);

    Event update(EventRequest request, Long id);

    Event cancel(Long id);

    List<Event> getEventByStatus(Status status);
}
