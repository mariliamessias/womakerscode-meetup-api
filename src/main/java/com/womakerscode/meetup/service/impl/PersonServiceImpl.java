package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.repository.AddressRepository;
import com.womakerscode.meetup.repository.PersonRepository;
import com.womakerscode.meetup.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Person save(PersonRequest request) {

        Address address = addressRepository.save(request.getAddress().toSaveAddress());

        return repository.save(request.toSavePerson(address));
    }

    @Override
    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

}
