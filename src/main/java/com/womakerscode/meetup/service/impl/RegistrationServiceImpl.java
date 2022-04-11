package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    RegistrationRepository repository;

    @Override
    public Registration save(RegistrationRequest registrationRequest) {

        if (repository.existsByUserIdAndEventId(registrationRequest.getUserId(), registrationRequest.getEventId())) {
            throw new BusinessException("Registration already created");
        }

        return repository.save(registrationRequest.toSaveRegistration());
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
