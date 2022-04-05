package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
