package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.User;

import java.util.List;

public interface UserService {
    User save(UserRequest usuario);

    List<Registration> findRegistrationsByUserId(Long id);
}
