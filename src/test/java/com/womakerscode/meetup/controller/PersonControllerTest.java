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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
                .andExpect(jsonPath("name").value(personRequest.getName()));

    }

    private PersonRequest createNewPerson() {
        return PersonRequest.builder()
                .name("test name test name")
                .birthDate(LocalDate.now())
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
