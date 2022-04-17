package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.data.UserDetail;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUserName(username);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User id: " + username + " not found");
        }

        return new UserDetail(user);
    }
}
