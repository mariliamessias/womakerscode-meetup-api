package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
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
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RegistrationRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RegistrationRepository repository;

    @Autowired
    private EventRepository eventRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Should save Registration with success from repository")
    public void testSave() {

        Registration registrationExpected = Registration.builder()
                .id(1L)
                .description("teste")
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        Registration registration = Registration.builder()
                .description("teste")
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        //execucao
        Registration registrationSaved = repository.save(registration);

        // assert
        Assertions.assertNotNull(registrationSaved, "Registration should not be null");
        Assertions.assertEquals(registrationSaved.getId(), registrationExpected.getId(), "Registrations id must be the same");
        Assertions.assertEquals(registrationSaved.getStatus(), registrationExpected.getStatus(), "Registrations status must be the same");
        Assertions.assertEquals(registrationSaved.getDescription(), registrationExpected.getDescription(), "Registrations description must be the same");

    }

    @Test
    @DisplayName("Should find Registrations by username from repository")
    public void testFindRegistrationsByUserName() {

        Registration registrationExpected = Registration.builder()
                .id(1L)
                .description("teste")
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        Registration registration = Registration.builder()
                .description("teste")
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .username("username")
                .build();

        repository.save(registration);
        //execucao
        List<Registration> registrationSaved = repository.findRegistrationByUsername("username");

        // assert
        Assertions.assertFalse(registrationSaved.isEmpty(), "Registration should not be empty");
        Assertions.assertEquals(registrationSaved.get(0).getId(), registrationExpected.getId(), "Registrations id must be the same");
        Assertions.assertEquals(registrationSaved.get(0).getStatus(), registrationExpected.getStatus(), "Registrations status must be the same");
        Assertions.assertEquals(registrationSaved.get(0).getDescription(), registrationExpected.getDescription(), "Registrations description must be the same");

    }

    @Test
    @DisplayName("Should delete Registration with success from repository")
    public void testDeleteRegistrationById() {
        Registration registration = Registration.builder()
                .description("teste")
                .status(Status.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(registration);
        //execucao
        Integer RegistrationsResult = repository.deleteRegistrationById(1L);

        // assert
        Assertions.assertNotNull(RegistrationsResult, "Registration should not be null");
        Assertions.assertEquals(RegistrationsResult, 1, "RegistrationsResult id must be the same");

    }

    @Test
    @DisplayName("Should not delete an Registration when don't exists from repository")
    public void testDeleteNotExecuted() {

        //execucao
        Integer RegistrationsResult = repository.deleteRegistrationById(55L);

        // assert
        Assertions.assertNotNull(RegistrationsResult, "Registration should not be null");
        Assertions.assertEquals(RegistrationsResult, 0, "RegistrationsResult id must be the same");

    }

    @Test
    @DisplayName("Should save a new registration in repository with user and event")
    public void testSaveWithUserAndEvent() {

        Event event = Event.builder().name("event name").status(Status.ACTIVE).alocatedSpots(50).maximunSpots(100).name("event test").build();

        Event eventSaved = eventRepository.save(event);

        Registration registration = RegistrationRequest.builder().description("test").build().toSaveRegistration(eventSaved);
        //execucao
        Registration registrationSaved = repository.save(registration);

        // assert
        Assertions.assertNotNull(registrationSaved, "Registration should not be null");
        Assertions.assertEquals(registrationSaved.getId(), 1L, "Registrations id must be the same");
        Assertions.assertEquals(registrationSaved.getStatus(), Status.CREATED, "Registrations status must be the same");
        Assertions.assertEquals(registrationSaved.getDescription(), "test", "Registrations description must be the same");

    }

    @Test
    @DisplayName("Validate if already have other registration by userId and eventId repository")
    public void testVerifyIfExistsRegistration() {

        //execucao
        Event event = Event.builder().name("event name 1").status(Status.ACTIVE).alocatedSpots(50).maximunSpots(100).name("event test 1").build();

        Event eventSaved = eventRepository.save(event);

        Registration registration = RegistrationRequest.builder().username("username").description("test").status(Status.ACTIVE).build().toSaveRegistration(eventSaved);

        repository.save(registration);

        //execucao
        Boolean result = repository.existsByUsernameAndEventId("username", 1L);

        // assert
        Assertions.assertNotNull(result, "Registration should not be null");
        Assertions.assertTrue(result, "Registration should exists");

    }
}
