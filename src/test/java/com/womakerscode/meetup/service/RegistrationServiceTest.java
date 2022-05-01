package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    @InjectMocks
    RegistrationServiceImpl registrationService;

    @Mock
    RegistrationRepository repository;

    @Mock
    UserRepository userRepository;

    @Mock
    EventRepository eventRepository;

    @Test
    @DisplayName("Should save an registration")
    public void saveRegistrationTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        Long userId = 1L;
        Long eventId = 1L;

        //cenario
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .description("test")
                .eventId(eventId)
                .userId(userId)
                .build();

        User user = User.builder().build();
        Event event = Event.builder().status(Status.CREATED).maximunSpots(10).alocatedSpots(0).build();
        //execução

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        when(repository.save(any(Registration.class))).thenReturn(buildRegistration(creationDate));

        Registration savedRegistration = registrationService.save(registrationRequest);

        //assert
        assertThat(savedRegistration.getCreatedAt()).isEqualTo(creationDate);
        assertThat(savedRegistration.getDescription()).isEqualTo("test");

    }

    @Test
    @DisplayName("Should get an registration by id")
    public void getRegistrationByIdTest() {
        LocalDateTime creationDate = LocalDateTime.now();

        //cenario
        Long id = 11L;
        Registration registration = buildRegistration(creationDate);
        registration.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(registration));

        // execução
        Optional<Registration> foundRegistration = registrationService.getRegistrationById(id);

        //asserts
        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(id);
        assertThat(foundRegistration.get().getCreatedAt()).isEqualTo(registration.getCreatedAt());

    }

    @Test
    @DisplayName("Should return empty when get an registration by id when doesn't exists")
    public void registrationNotFoundByIdTest() {

        //cenario
        Long id = 11L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Registration> registration = registrationService.getRegistrationById(id);

        //asserts
        assertThat(registration.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Should delete an registration")
    public void deleteRegistrationTest() {

        //cenario
        Long id = 11L;
        when(repository.deleteRegistrationById(id)).thenReturn(1);

        //execução
        assertDoesNotThrow(() -> registrationService.delete(id));

        //asserts
        verify(repository, Mockito.times(1)).deleteRegistrationById(id);

    }

    @Test
    @DisplayName("Should update an registration")
    public void updateRegistrationTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        Long userId = 1L;
        Long eventId = 1L;

        //cenario
        Long id = 101L;

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .description("test")
                .eventId(eventId)
                .userId(userId)
                .build();

        //execução
        Registration updatedRegistration = buildRegistration(creationDate);
        updatedRegistration.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(updatedRegistration));
        when(repository.save(any(Registration.class))).thenReturn(updatedRegistration);
        Registration registration = registrationService.update(registrationRequest, id);

        //asserts
        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        assertThat(registration.getCreatedAt()).isEqualTo(updatedRegistration.getCreatedAt());

    }

    @Test
    @DisplayName("Should filter registrations must by properties")
    public void findRegistrationTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        // cenário
        Registration registration = buildRegistration(creationDate);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Registration> listRegistrations = singletonList(registration);

        Page<Registration> page = new PageImpl<>(singletonList(registration), pageRequest, 1);

        //execução
        when(repository.findAll(any(), any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.find(registration, pageRequest);

        //asserts
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistrations);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }


    private Registration buildRegistration(LocalDateTime creationDate) {
        return Registration.builder()
                .description("test")
                .createdAt(creationDate)
                .build();
    }
}
