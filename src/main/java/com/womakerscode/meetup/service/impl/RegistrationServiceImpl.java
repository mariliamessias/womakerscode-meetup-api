package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.NotAllowedException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.SendEmaillMessage;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.repository.PersonRepository;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.PublisherService;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    public static final String REGISTRATION = "REGISTRATION";
    @Autowired
    RegistrationRepository repository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PublisherService publisherService;

    @Override
    public Registration save(RegistrationRequest registrationRequest) {

        if (repository.existsByUsernameAndEventId(registrationRequest.getUsername(), registrationRequest.getEventId())) {
            throw new BusinessException("Registration already created");
        }

        Event event = findEvent(registrationRequest.getEventId());
        event.setAlocatedSpots(event.getAlocatedSpots() + 1);

        Person person = findPerson(registrationRequest.getUsername());

        if (event.getMaximunSpots() <= (event.getAlocatedSpots())) {
            event.setStatus(Status.FULL);
        }

        eventRepository.save(event);
        Registration result = repository.save(registrationRequest.toSaveRegistration(event));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        publisherService.publish(SendEmaillMessage.builder()
                .email(person.getEmail())
                .eventName(event.getName())
                .name(person.getName())
                .eventDate(event.getEventDate().format(formatter))
                .type(REGISTRATION)
                .build());

        return result;
    }

    private Person findPerson(String userName) {
        return personRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("Person username: " + userName + " not found"));
    }

    private Event findEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event id: " + id + " not found"));

        if (event.getStatus().equals(Status.FULL) || event.getMaximunSpots() <= (event.getAlocatedSpots())) {
            throw new NotAllowedException("Event id: " + id + " is not empty.");
        }

        if (event.getStatus().equals(Status.CANCELED)) {
            throw new NotAllowedException("Event id: " + id + " is already canceled");
        }

        return event;
    }

    @Override
    public Optional<Registration> getRegistrationById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Long id) {

        Integer result = repository.deleteRegistrationById(id);

        if (result == 0) {
            throw new BusinessException("Fail to delete registration with id: " + id);
        }
    }

    @Override
    public Registration update(RegistrationRequest registration, Long id) {
        return repository.findById(id).map(result -> {

            result.setDescription(registration.getDescription());
            result.setStatus(registration.getStatus());

            return repository.save(result);

        }).orElseThrow(() -> new ResourceNotFoundException("Registration id: " + id + " not found"));

    }

    @Override
    public Page<Registration> find(Registration filter, Pageable pageRequest) {
        Example<Registration> example = Example.of(filter, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

}
