package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Registration;

import java.util.Optional;

public interface RegistrationService {

    Registration save (Registration registration);
    Optional<Registration> getRegistrationById(Long id);
}
