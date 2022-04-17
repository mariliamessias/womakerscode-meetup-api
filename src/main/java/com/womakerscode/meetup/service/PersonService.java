package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Person;

import java.util.Optional;

public interface PersonService {

    Person save(PersonRequest request);
    Optional<Person> getPersonById(Long id);
}
