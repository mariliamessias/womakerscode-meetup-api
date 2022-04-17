package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.UserResponse;
import com.womakerscode.meetup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse create(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest).toUserResponse();
    }
}
