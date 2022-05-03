package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RegistrationService {

    Registration save(RegistrationRequest registrationRequest);

    Optional<Registration> getRegistrationById(Long id);

    List<Registration> getRegistrationByUserName(String username);

    void delete(Long id);

    Registration update(RegistrationRequest registrationRequest, Long id);

    Page<Registration> find(Registration filter, Pageable pageRequest);
}
