package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface RegistrationService {

    Registration save (Registration registration);

    Optional<Registration> getRegistrationById(Long id);

    void delete(Registration registration);

    Registration update(Registration registration);

    Page<Registration> find(Registration filter, PageRequest pageRequest);

    Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute);
}
