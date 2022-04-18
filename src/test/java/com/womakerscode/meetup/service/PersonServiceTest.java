package com.womakerscode.meetup.service;


import com.womakerscode.meetup.model.AddressRequest;
import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.model.entity.Role;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.repository.AddressRepository;
import com.womakerscode.meetup.repository.PersonRepository;
import com.womakerscode.meetup.repository.UserRepository;
import com.womakerscode.meetup.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PersonServiceTest {

    @InjectMocks
    PersonServiceImpl personService;

    @Mock
    PersonRepository repository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Should save a person")
    public void savePersonTest() {
        LocalDateTime creationDate = LocalDateTime.now();
        //cenario
        PersonRequest personRequest = PersonRequest.builder()
                .name("name")
                .email("email")
                .userId(1L)
                .birthDate(LocalDate.now())
                .address(AddressRequest
                        .builder()
                        .city("Test City")
                        .country("Test Country")
                        .neighborhood("Test neighborhood")
                        .number(123)
                        .build())
                .build();

        //execução
        when(repository.save(any())).thenReturn(buildPerson(creationDate));

        when(addressRepository.save(any())).thenReturn(buildAddress(creationDate));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(buildUser(creationDate)));

        Person savedPerson = personService.save(personRequest);

        //assert
        assertThat(savedPerson.getName()).isEqualTo("name");
        assertThat(savedPerson.getBirthDate()).isEqualTo(LocalDate.now());
        assertThat(savedPerson.getEmail()).isEqualTo("email");
        assertThat(savedPerson.getCreatedAt()).isEqualTo(creationDate);
    }

    private Person buildPerson(LocalDateTime creationDate) {
        return Person.builder()
                .createdAt(creationDate)
                .name("name")
                .email("email")
                .birthDate(LocalDate.now())
                .build();
    }

    private Address buildAddress(LocalDateTime creationDate) {
        return Address.builder()
                .id(11L)
                .createdAt(creationDate)
                .city("Test City")
                .country("Test Country")
                .neighborhood("Test neighborhood")
                .number(123)
                .build();
    }


    private User buildUser(LocalDateTime creationDate) {
        return User.builder()
                .userName("username")
                .password("124")
                .role(Role.ADMIN)
                .createdAt(creationDate)
                .build();
    }
}
