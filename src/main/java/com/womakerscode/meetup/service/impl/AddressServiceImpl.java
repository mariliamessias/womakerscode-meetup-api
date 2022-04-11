package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.repository.AddressRepository;
import com.womakerscode.meetup.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository repository;

    @Override
    public Address save(Address address) {
        return repository.save(address);
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        return repository.findById(id);
    }
}
