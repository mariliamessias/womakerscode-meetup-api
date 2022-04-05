package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Transactional
    Integer deleteRegistrationById(Long id);

}
