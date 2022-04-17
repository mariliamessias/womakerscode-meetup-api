package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.UserResponse;
import com.womakerscode.meetup.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User save(UserRequest usuario);
}
