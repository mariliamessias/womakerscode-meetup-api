package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.RegistrationResponse;
import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.UserResponse;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest userRequest) {
        return userService.save(userRequest).toUserResponse();
    }

    @GetMapping("{id}/registration")
    @ResponseStatus(HttpStatus.OK)
    public List<RegistrationResponse> findRegistrationByUserId(@PathVariable Long id) {
        return userService.findRegistrationsByUserId(id)
                .stream().map(Registration::toRegistrationResponse).collect(Collectors.toList());

    }
}
