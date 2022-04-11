package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.model.entity.Status;
import com.womakerscode.meetup.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class RegistrationRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RegistrationRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DataSet("registration.yml")
    @DisplayName("Should save Registration with success from repository")
    public void testFindAllSuccess() {

        Registration registrationExpected = Registration.builder()
                .id(2L)
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
    @DataSet("registration.yml")
    @DisplayName("Should delete Registration with success from repository")
    public void testDeleteSuccess() {

        //execucao
        Integer RegistrationsResult = repository.deleteRegistrationById(1L);

        // assert
        Assertions.assertNotNull(RegistrationsResult, "Registration should not be null");
        Assertions.assertEquals(RegistrationsResult, 1, "RegistrationsResult id must be the same");

    }

    @Test
    @DataSet("registration.yml")
    @DisplayName("Should not delete an Registration when don't exists from repository")
    public void testDeleteNotExecuted() {

        //execucao
        Integer RegistrationsResult = repository.deleteRegistrationById(55L);

        // assert
        Assertions.assertNotNull(RegistrationsResult, "Registration should not be null");
        Assertions.assertEquals(RegistrationsResult, 0, "RegistrationsResult id must be the same");

    }

    @Test
    @DataSet("registration.yml")
    @DisplayName("Should save a new registration in repository")
    public void testSaveRegistration() {

        Registration registration = RegistrationRequest.builder().description("test").build().toSaveRegistration();
        //execucao
        Registration registrationSaved = repository.save(registration);

        // assert
        Assertions.assertNotNull(registrationSaved, "Registration should not be null");
        Assertions.assertEquals(registrationSaved.getId(), 2L, "Registrations id must be the same");
        Assertions.assertEquals(registrationSaved.getStatus(), Status.CREATED, "Registrations status must be the same");
        Assertions.assertEquals(registrationSaved.getDescription(), "test", "Registrations description must be the same");

    }

    @Test
    @DataSet("registration.yml")
    @DisplayName("Validate if already have other registration by userId and eventId repository")
    public void testVerifyIfExistsRegistration() {

        Registration registration = RegistrationRequest.builder().description("test").build().toSaveRegistration();
        User user = User.builder().id(1L).build();
        Event event = Event.builder().id(1L).build();


        //execucao
        User userSaved = userRepository.save(user);
        Event eventSaved = eventRepository.save(event);

        registration.setUser(userSaved);
        registration.setEvent(eventSaved);
        Registration registrationSaved = repository.save(registration);

        //execucao
        Boolean result = repository.existsByUserIdAndEventId(1L, 2L);

        // assert
        Assertions.assertNotNull(result, "Registration should not be null");
        Assertions.assertTrue(result, "Registration should exists");

    }
}
