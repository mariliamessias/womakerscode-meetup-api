package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Address;

import java.util.Optional;

public interface AddressService {

    Address save(Address address);

    Optional<Address> getAddressById(Long id);
}
