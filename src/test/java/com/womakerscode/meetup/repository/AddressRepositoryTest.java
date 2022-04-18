package com.womakerscode.meetup.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.womakerscode.meetup.model.entity.Address;
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
import java.util.Optional;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddressRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AddressRepository repository;

    public ConnectionHolder getConnectionHolder() {
        return () -> dataSource.getConnection();
    }

    @Test
    @DisplayName("Should save Address with success from repository")
    public void testSaveSuccess() {

        Address addresExpected = Address.builder()
                .id(1L)
                .publicPlace("test publicPlace")
                .neighborhood("test neighborhood")
                .build();

        Address address = Address.builder()
                .publicPlace("test publicPlace")
                .neighborhood("test neighborhood")
                .build();
        //execucao
        Address addressSaved = repository.save(address);

        // assert
        Assertions.assertNotNull(addressSaved, "Address should not be null");
        Assertions.assertEquals(addressSaved.getId(), addresExpected.getId(), "Address id must be the same");
        Assertions.assertEquals(addressSaved.getPublicPlace(), addresExpected.getPublicPlace(), "Address public place must be the same");
        Assertions.assertEquals(addressSaved.getNeighborhood(), addresExpected.getNeighborhood(), "Registrations neighborhood must be the same");

    }

    @Test
    @DisplayName("Should find Address by Id with success from repository")
    public void testFindAddressById() {

        Address addresExpected = Address.builder()
                .id(1L)
                .publicPlace("test publicPlace")
                .neighborhood("test neighborhood")
                .build();

        Address address = Address.builder()
                .publicPlace("test publicPlace")
                .neighborhood("test neighborhood")
                .build();

        repository.save(address);
        //execucao
        Optional<Address> result = repository.findById(1L);

        // assert
        Assertions.assertNotNull(result, "Address should not be null");
        Assertions.assertTrue(result.isPresent(), "Address should not be empty");

        Assertions.assertEquals(result.get().getId(), addresExpected.getId(), "Address id must be the same");
        Assertions.assertEquals(result.get().getPublicPlace(), addresExpected.getPublicPlace(), "Address public place must be the same");
        Assertions.assertEquals(result.get().getNeighborhood(), addresExpected.getNeighborhood(), "Registrations neighborhood must be the same");

    }
}
