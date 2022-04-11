package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public Person save(PersonRequest request) {
        return null;
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Person update(PersonRequest request, Long id) {
        return null;
    }
}
