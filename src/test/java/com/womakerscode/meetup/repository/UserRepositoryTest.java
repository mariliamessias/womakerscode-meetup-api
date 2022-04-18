package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.User;
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
public class UserRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Should save User with success from repository")
    public void testSaveSuccess() {

        User userExpected = User.builder()
                .id(1L)
                .userName("testname")
                .role(Role.ADMIN)
                .password("1324")
                .build();

        User user = User.builder()
                .userName("testname")
                .role(Role.ADMIN)
                .password("1324")
                .build();

        //execucao
        User userSaved = repository.save(user);

        // assert
        Assertions.assertNotNull(userSaved, "User should not be null");
        Assertions.assertEquals(userSaved.getId(), userExpected.getId(), "User id must be the same");
        Assertions.assertEquals(userSaved.getRole(), userExpected.getRole(), "User role must be the same");
        Assertions.assertEquals(userSaved.getUserName(), userExpected.getUserName(), "User userName must be the same");
        Assertions.assertEquals(userSaved.getPassword(), userExpected.getPassword(), "User password must be the same");

    }
}
