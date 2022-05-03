package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository repository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Override
    public Event save(EventRequest eventRequest) {
        return repository.save(eventRequest.toSaveEvent());
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Long id) {

        Integer result = repository.deleteEventById(id);

        if (result == 0) {
            throw new BusinessException("Fail to delete event with id: " + id);
        }
    }

    @Override
    public Event update(EventRequest request, Long id) {

        return repository.findById(id).map(result -> {

            if (request.getName() != null) result.setName(request.getName());
            if (request.getMaximunSpots() != null) result.setMaximunSpots(request.getMaximunSpots());
            if (request.getAlocatedSpots() != null) result.setAlocatedSpots(request.getAlocatedSpots());

            return repository.save(result);

        }).orElseThrow(() -> new ResourceNotFoundException("Event id: " + id + " not found"));
    }

    @Override
    public Event cancel(Long id) {
        return repository.findById(id).map(result -> {

            result.setStatus(Status.CANCELED);
            result.getRegistrations().forEach(registration -> {
                registration.setStatus(Status.CANCELED);
                registrationRepository.save(registration);
            });

            return repository.save(result);

        }).orElseThrow(() -> new ResourceNotFoundException("Event id: " + id + " not found"));
    }

    @Override
    public List<Event> getEventByStatus(Status status) {
        return repository.findEventByStatus(status);
    }
}
