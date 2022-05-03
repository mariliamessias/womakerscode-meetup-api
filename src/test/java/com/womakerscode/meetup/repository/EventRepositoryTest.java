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
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:application-test.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
    public void testSave() {

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

    @Test
    @DisplayName("Should get Event by id with success from repository")
    public void testGetEventById() {

        long id = 1;

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

        repository.save(event);
        //execucao
        Optional<Event> eventSaved = repository.findById(id);

        // assert
        Assertions.assertNotNull(eventSaved, "Event should not be null");
        Assertions.assertTrue(eventSaved.isPresent(), "Event should not be empty");

        Assertions.assertEquals(eventSaved.get().getId(), eventExpected.getId(), "Event id must be the same");
        Assertions.assertEquals(eventSaved.get().getAlocatedSpots(), eventExpected.getAlocatedSpots(), "Event AlocatedSpots must be the same");
        Assertions.assertEquals(eventSaved.get().getMaximunSpots(), eventExpected.getMaximunSpots(), "Event MaximunSpots must be the same");

    }

    @Test
    @DisplayName("Should get empty Event by id with success from repository")
    public void testGetEventByIdEmpty() {

        long id = 55;
        //execucao
        Optional<Event> eventSaved = repository.findById(id);

        // assert
        Assertions.assertNotNull(eventSaved, "Event should not be null");
        Assertions.assertTrue(eventSaved.isEmpty(), "Event should be empty");

    }

    @Test
    @DisplayName("Should delete Event by id with success from repository")
    public void testDeleteEventById() {

        long id = 1;

        Event event = Event.builder()
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(10)
                .build();

        repository.save(event);
        //execucao
        Integer response = repository.deleteEventById(id);

        // assert
        Assertions.assertNotNull(response, "Event should not be null");
        Assertions.assertEquals(response, 1, "Event response must be the same");

    }

    @Test
    @DisplayName("Should not delete Event by id")
    public void testFailToDeleteEventById() {

        long id = 1;

        //execucao
        Integer response = repository.deleteEventById(id);

        // assert
        Assertions.assertNotNull(response, "Event should not be null");
        Assertions.assertEquals(response, 0, "Event response must be the same");

    }

    @Test
    @DisplayName("Should update Event with success from repository")
    public void testUpdateEvent() {

        Event eventSaved = Event.builder()
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(10)
                .build();

        Event event = Event.builder()
                .id(1L)
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(50)
                .build();

        Event eventExpected = Event.builder()
                .id(1L)
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(50)
                .build();

        repository.save(eventSaved);
        //execucao
        Event response = repository.save(event);

        // assert
        Assertions.assertNotNull(response, "Event should not be null");
        Assertions.assertEquals(response.getId(), eventExpected.getId(), "Event id must be the same");
        Assertions.assertEquals(response.getAlocatedSpots(), eventExpected.getAlocatedSpots(), "Event AlocatedSpots must be the same");
        Assertions.assertEquals(response.getMaximunSpots(), eventExpected.getMaximunSpots(), "Event MaximunSpots must be the same");

    }

    @Test
    @DisplayName("Should update Event to canceled with success from repository")
    public void testCancelEvent() {

        Event eventSaved = Event.builder()
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(10)
                .build();

        Event event = Event.builder()
                .id(1L)
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(50)
                .build();

        Event eventExpected = Event.builder()
                .id(1L)
                .name("name")
                .alocatedSpots(0)
                .maximunSpots(50)
                .build();

        repository.save(eventSaved);
        //execucao
        Event response = repository.save(event);

        // assert
        Assertions.assertNotNull(response, "Event should not be null");
        Assertions.assertEquals(response.getId(), eventExpected.getId(), "Event id must be the same");
        Assertions.assertEquals(response.getAlocatedSpots(), eventExpected.getAlocatedSpots(), "Event AlocatedSpots must be the same");
        Assertions.assertEquals(response.getMaximunSpots(), eventExpected.getMaximunSpots(), "Event MaximunSpots must be the same");

    }
}
