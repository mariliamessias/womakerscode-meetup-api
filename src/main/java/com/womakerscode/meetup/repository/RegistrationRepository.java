package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Boolean existsByRegistration(String registration);
}
