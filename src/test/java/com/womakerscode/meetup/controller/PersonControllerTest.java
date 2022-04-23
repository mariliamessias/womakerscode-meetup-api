package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.configs.Properties;
import com.womakerscode.meetup.model.AddressRequest;
import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {PersonController.class})
@AutoConfigureMockMvc
public class PersonControllerTest {

    static String PERSON_API = "/person";

    @MockBean
    PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Properties properties;

    @Autowired
    MockMvc mockMvc;

    public static final int TOKEN_EXPIRATION = 600_000;

    @Test
    @DisplayName("Should create a person with success")
    public void createPersonTest() throws Exception {

        //cenário
        PersonRequest personRequest = createNewPerson();
        Person person = Person.builder()
                .name("test name test name")
                .birthDate(LocalDate.now())
                .email("email@email.com")
                .createdAt(LocalDateTime.now())
                .address(Address.builder()
                        .city("city test")
                        .country("country test")
                        .neighborhood("neighborhood test")
                        .number(123)
                        .publicPlace("public test")
                        .zipCode("1233")
                        .build())
                .build();

        // execução
        BDDMockito.given(personService.save(any(PersonRequest.class))).willReturn(person);

        String json = objectMapper.writeValueAsString(personRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PERSON_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(personRequest.getName()))
                .andExpect(jsonPath("address.city").value(personRequest.getAddress().getCity()))
                .andExpect(jsonPath("address.country").value(personRequest.getAddress().getCountry()))
                .andExpect(jsonPath("address.neighborhood").value(personRequest.getAddress().getNeighborhood()))
                .andExpect(jsonPath("address.number").value(personRequest.getAddress().getNumber()))
                .andExpect(jsonPath("address.public_place").value(personRequest.getAddress().getPublicPlace()))
                .andExpect(jsonPath("address.zip_code").value(personRequest.getAddress().getZipCode()))
                .andExpect(jsonPath("birth_date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("email").value(person.getEmail()));

    }

    @Test
    @DisplayName("Should not create a person with success when payload is invalid")
    public void badRequestCreatePersonTest() throws Exception {
        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PERSON_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should get a person by id with success")
    public void getPersonByidTest() throws Exception {
        long id = 1L;

        //cenário
        Person person = Person.builder()
                .name("test name test name")
                .birthDate(LocalDate.now())
                .email("email@email.com")
                .createdAt(LocalDateTime.now())
                .address(Address.builder()
                        .city("city test")
                        .country("country test")
                        .neighborhood("neighborhood test")
                        .number(123)
                        .publicPlace("public test")
                        .zipCode("1233")
                        .build())
                .build();

        // execução
        BDDMockito.given(personService.getPersonById(eq(id))).willReturn(Optional.of(person));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PERSON_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(person.getName()))
                .andExpect(jsonPath("address.city").value(person.getAddress().getCity()))
                .andExpect(jsonPath("address.country").value(person.getAddress().getCountry()))
                .andExpect(jsonPath("address.neighborhood").value(person.getAddress().getNeighborhood()))
                .andExpect(jsonPath("address.number").value(person.getAddress().getNumber()))
                .andExpect(jsonPath("address.public_place").value(person.getAddress().getPublicPlace()))
                .andExpect(jsonPath("address.zip_code").value(person.getAddress().getZipCode()))
                .andExpect(jsonPath("birth_date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("email").value(person.getEmail()));

    }

    @Test
    @DisplayName("Should not get a person by id when not found event")
    public void notFoundErrorGetPersonByidTest() throws Exception {
        long id = 1L;

        // execução
        BDDMockito.given(personService.getPersonById(eq(id))).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PERSON_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    private PersonRequest createNewPerson() {
        return PersonRequest.builder()
                .name("test name test name")
                .birthDate(LocalDate.now())
                .userId(1L)
                .email("email@email.com")
                .address(AddressRequest.builder()
                        .city("city test")
                        .country("country test")
                        .neighborhood("neighborhood test")
                        .number(123)
                        .publicPlace("public test")
                        .zipCode("1233")
                        .build())
                .build();
    }
}
