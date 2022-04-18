package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.PersonRepository;
import com.womakerscode.meetup.repository.UserRepository;
import com.womakerscode.meetup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public User save(UserRequest userRequest) {

        if (userRepository.existsByUserName(userRequest.getUserName())) {
            throw new BusinessException("user already created");
        }

        userRequest.setPassword(encoder.encode(userRequest.getPassword()));
        if (userRequest.getRole() == null) userRequest.setRole(Role.NORMAL);

        return personRepository.findById(userRequest.getPersonId())
                .map(result -> userRepository.save(userRequest.toSaveUser(result)))
                .orElseThrow(() -> new ResourceNotFoundException("Person id: " + userRequest.getPersonId() + " not found"));

    }

    @Override
    public List<Registration> findRegistrationsByUserId(Long id) {
        return userRepository.findById(id).map(User::getRegistrations)
                .orElseThrow(() -> new ResourceNotFoundException("Registrations for userId: " + id + " not found"));
    }
}
