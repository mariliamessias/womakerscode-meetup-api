package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.entity.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EventRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EventRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Should save Event with success from repository")
    public void testSaveSuccess() {

        Event eventExpected = Event.builder()
                .id(1L)
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(10)
                .build();

        Event event = Event.builder()
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(10)
                .build();

        //execucao
        Event eventSaved = repository.save(event);

        // assert
        Assertions.assertNotNull(eventSaved, "Event should not be null");
        Assertions.assertEquals(eventSaved.getId(), eventExpected.getId(), "Event id must be the same");
        Assertions.assertEquals(eventSaved.getAlocatedSpots(), eventExpected.getAlocatedSpots(), "Event AlocatedSpots must be the same");
        Assertions.assertEquals(eventSaved.getMaximunSpots(), eventExpected.getMaximunSpots(), "Event MaximunSpots must be the same");

    }
}
