package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.PersonRepository;
import com.womakerscode.meetup.repository.UserRepository;
import com.womakerscode.meetup.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository repository;

    @Mock
    PasswordEncoder encoder;

    @Test
    @DisplayName("Should save an user")
    public void saveUserTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        //cenario
        UserRequest userRequest = UserRequest.builder()
                .userName("username")
                .password("124")
                .role(Role.ADMIN)
                .build();

        //execução
        when(repository.save(any())).thenReturn(buildUser(creationDate));

        User savedUser = userService.save(userRequest);

        //assert
        assertThat(savedUser.getUserName()).isEqualTo("username");
        assertThat(savedUser.getPassword()).isEqualTo("124");
        assertThat(savedUser.getRole()).isEqualTo(Role.ADMIN);
        assertThat(savedUser.getCreatedAt()).isEqualTo(creationDate);
    }

    private User buildUser(LocalDateTime creationDate) {
        return User.builder()
                .userName("username")
                .password("124")
                .role(Role.ADMIN)
                .createdAt(creationDate)
                .build();
    }

}
