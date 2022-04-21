package com.womakerscode.meetup.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.configs.Properties;
import com.womakerscode.meetup.data.UserDetail;
import com.womakerscode.meetup.model.AddressRequest;
import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.entity.Address;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.service.PersonService;
import com.womakerscode.meetup.service.impl.UserDetailServiceImpl;
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
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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
    private UserDetailServiceImpl userDetailService;

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

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(personService.save(any(PersonRequest.class))).willReturn(person);

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        String json = objectMapper.writeValueAsString(personRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PERSON_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail))
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

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PERSON_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should not create a person when has not Authorization token")
    public void forbiddenCreatePersonTest() throws Exception {

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

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(personService.save(any(PersonRequest.class))).willReturn(person);

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        String json = objectMapper.writeValueAsString(personRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PERSON_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

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

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(personService.getPersonById(eq(id))).willReturn(Optional.of(person));

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PERSON_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

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

        //cenário
        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(personService.getPersonById(eq(id))).willReturn(Optional.empty());

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PERSON_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should not get a person by id when has not Authorization token")
    public void forbiddenErrorGetPersonByidTest() throws Exception {
        long id = 1L;

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PERSON_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

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

    private String buildToken(UserDetail userDetail) {

        BDDMockito.given(properties.getProperty(anyString())).willReturn("9ebf2b42-3a5a-4193-ac50-73ea5547af27");

        return JWT.create()
                .withSubject(userDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(properties.getProperty("token.password")));
    }
}
