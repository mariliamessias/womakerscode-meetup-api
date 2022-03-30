package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository repository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Registration save(Registration registration) {
        if (repository.existsByRegistration(registration.getRegistration())) {
            throw new BusinessException("Registration already created");
        }

        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void delete(Registration registration) {
        if (isNull(registration) || isNull(registration.getId())) {
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        repository.delete(registration);
    }

    @Override
    public Registration update(Registration registration) {
        if (isNull(registration) || isNull(registration.getId())) {
            throw new IllegalArgumentException("Registration id cannot be null");
        }
        return repository.save(registration);
    }

    @Override
    public Page<Registration> find(Registration filter, PageRequest pageRequest) {
        Example<Registration> example = Example.of(filter, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute) {
        return repository.findByRegistration(registrationAttribute);
    }
}
