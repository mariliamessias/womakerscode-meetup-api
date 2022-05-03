package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.EventServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EventServiceTest {

    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    EventRepository repository;

    @Mock
    RegistrationRepository registrationRepository;

    @Test
    @DisplayName("Should save an event")
    public void saveEventTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        //cenario

        EventRequest eventRequest = EventRequest.builder()
                .maximunSpots(10)
                .alocatedSpots(0)
                .name("name")
                .build();

        //execução
        when(repository.save(any())).thenReturn(buildEvent(creationDate));

        Event savedEvent = eventService.save(eventRequest);

        //assert
        assertThat(savedEvent.getAlocatedSpots()).isEqualTo(0);
        assertThat(savedEvent.getMaximunSpots()).isEqualTo(10);
        assertThat(savedEvent.getName()).isEqualTo("name");
        assertThat(savedEvent.getCreatedAt()).isEqualTo(creationDate);
    }

    @Test
    @DisplayName("Should get an event by Id")
    public void getEventByIdTest() {
        long id = 1L;
        LocalDateTime creationDate = LocalDateTime.now();

        //execução
        when(repository.findById(eq(id))).thenReturn(Optional.of(buildEvent(creationDate)));

        Optional<Event> savedEvent = eventService.getEventById(id);

        //assert
        assertThat(savedEvent.get().getAlocatedSpots()).isEqualTo(0);
        assertThat(savedEvent.get().getMaximunSpots()).isEqualTo(10);
        assertThat(savedEvent.get().getName()).isEqualTo("name");
        assertThat(savedEvent.get().getCreatedAt()).isEqualTo(creationDate);
    }

    @Test
    @DisplayName("Should get an event by status")
    public void getEventByStatusTest() {
        LocalDateTime creationDate = LocalDateTime.now();

        //execução
        when(repository.findEventByStatus(eq(Status.ACTIVE))).thenReturn(Collections.singletonList(buildEvent(creationDate)));

        List<Event> savedEvent = eventService.getEventByStatus(Status.ACTIVE);

        //assert
        assertThat(savedEvent.size()).isEqualTo(1);
        assertThat(savedEvent.get(0).getAlocatedSpots()).isEqualTo(0);
        assertThat(savedEvent.get(0).getMaximunSpots()).isEqualTo(10);
        assertThat(savedEvent.get(0).getName()).isEqualTo("name");
        assertThat(savedEvent.get(0).getCreatedAt()).isEqualTo(creationDate);
    }

    @Test
    @DisplayName("Should delete an event by Id")
    public void deleteEventByIdTest() {
        long id = 1L;
        //execução
        when(repository.deleteEventById(id)).thenReturn(1);

        assertDoesNotThrow(() -> eventService.delete(id));

        //asserts
        verify(repository, Mockito.times(1)).deleteEventById(id);
    }

    @Test
    @DisplayName("Should update an event by Id")
    public void updateEventByIdTest() {
        long id = 1L;
        LocalDateTime creationDate = LocalDateTime.now();

        EventRequest eventRequest = EventRequest.builder()
                .maximunSpots(10)
                .alocatedSpots(0)
                .name("name")
                .build();

        //execução
        when(repository.findById(eq(id))).thenReturn(Optional.of(buildEvent(creationDate)));
        when(repository.save(any(Event.class))).thenReturn(buildEvent(creationDate));

        Event savedEvent = eventService.update(eventRequest, id);

        //assert
        assertThat(savedEvent.getAlocatedSpots()).isEqualTo(0);
        assertThat(savedEvent.getMaximunSpots()).isEqualTo(10);
        assertThat(savedEvent.getName()).isEqualTo("name");
        assertThat(savedEvent.getCreatedAt()).isEqualTo(creationDate);
    }

    @Test
    @DisplayName("Should cancel an event by Id")
    public void cancelEventByIdTest() {
        long id = 1L;
        LocalDateTime creationDate = LocalDateTime.now();
        Registration registration = buildRegistration(creationDate);

        //execução
        when(repository.findById(eq(id))).thenReturn(Optional.of(buildEvent(creationDate)));
        when(repository.save(any(Event.class))).thenReturn(buildEvent(creationDate));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Event savedEvent = eventService.cancel(id);

        //assert
        assertThat(savedEvent.getAlocatedSpots()).isEqualTo(0);
        assertThat(savedEvent.getMaximunSpots()).isEqualTo(10);
        assertThat(savedEvent.getName()).isEqualTo("name");
        assertThat(savedEvent.getCreatedAt()).isEqualTo(creationDate);
    }

    private Event buildEvent(LocalDateTime creationDate) {
        return Event.builder()
                .maximunSpots(10)
                .alocatedSpots(0)
                .name("name")
                .createdAt(creationDate)
                .registrations(Collections.singletonList(buildRegistration(creationDate)))
                .build();
    }

    private Registration buildRegistration(LocalDateTime creationDate) {
        return Registration.builder()
                .description("test")
                .createdAt(creationDate)
                .build();
    }
}
