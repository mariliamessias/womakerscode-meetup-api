package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.entity.Person;
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
import java.time.LocalDate;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PersonRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersonRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Should save Person with success from repository")
    public void testSaveSuccess() {

        Person personExpected = Person.builder()
                .id(1L)
                .name("name")
                .email("email.com")
                .birthDate(LocalDate.now())
                .build();

        Person person = Person.builder()
                .name("name")
                .email("email.com")
                .birthDate(LocalDate.now())
                .build();

        //execucao
        Person personSaved = repository.save(person);

        // assert
        Assertions.assertNotNull(personSaved, "Person should not be null");
        Assertions.assertEquals(personSaved.getId(), personExpected.getId(), "Person id must be the same");
        Assertions.assertEquals(personSaved.getEmail(), personExpected.getEmail(), "Person email must be the same");
        Assertions.assertEquals(personSaved.getName(), personExpected.getName(), "Person name must be the same");
        Assertions.assertEquals(personSaved.getBirthDate(), personExpected.getBirthDate(), "Person BirthDate must be the same");
    }
}
