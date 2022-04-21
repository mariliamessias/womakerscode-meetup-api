package com.womakerscode.meetup.service;

import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.model.entity.User;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("Should find registrations by user id")
    public void findRegistrationsByUserIdTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        //execução
        when(repository.findById(any())).thenReturn(Optional.of(buildUser(creationDate)));

        List<Registration> registrations = userService.findRegistrationsByUserId(1L);

        //assert
        assertThat(registrations.size()).isEqualTo(1);
        assertThat(registrations.get(0).getDescription()).isEqualTo("test");
        assertThat(registrations.get(0).getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(registrations.get(0).getCreatedAt()).isEqualTo(creationDate);

    }

    @Test
    @DisplayName("Should not find registrations by user id")
    public void notFoundfindRegistrationsByUserIdTest() {
        //execução
        when(repository.findById(any())).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> userService.findRegistrationsByUserId(1L));
        //assert
        assertEquals("Registrations for userId: 1 not found", exception.getMessage());

    }

    private User buildUser(LocalDateTime creationDate) {
        return User.builder()
                .userName("username")
                .password("124")
                .role(Role.ADMIN)
                .registrations(Collections.singletonList(buildRegistration(creationDate)))
                .createdAt(creationDate)
                .build();
    }

    private Registration buildRegistration(LocalDateTime creationDate) {
        return Registration.builder()
                .description("test")
                .status(Status.ACTIVE)
                .createdAt(creationDate)
                .build();
    }

}
