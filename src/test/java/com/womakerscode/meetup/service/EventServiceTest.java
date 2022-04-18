package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.repository.EventRepository;
import com.womakerscode.meetup.service.impl.EventServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EventServiceTest {

    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    EventRepository repository;

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

    private Event buildEvent(LocalDateTime creationDate) {
        return Event.builder()
                .maximunSpots(10)
                .alocatedSpots(0)
                .name("name")
                .createdAt(creationDate)
                .build();
    }
}
