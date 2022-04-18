package com.womakerscode.meetup.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.configs.Properties;
import com.womakerscode.meetup.data.UserDetail;
import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.service.EventService;
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

import java.util.Date;
import java.util.Optional;

import static com.womakerscode.meetup.model.entity.Status.ACTIVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {EventController.class})
@AutoConfigureMockMvc
public class EventControllerTest {

    static String EVENT_API = "/event";

    @MockBean
    EventService eventService;

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
    @DisplayName("Should create an event with success")
    public void createEventTest() throws Exception {

        //cenário
        EventRequest eventRequest = createNewEvent();
        Event event = Event.builder()
                .status(ACTIVE)
                .name("test name")
                .build();

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(eventService.save(any(EventRequest.class))).willReturn(event);

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        String json = objectMapper.writeValueAsString(eventRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EVENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail))
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(jsonPath("name").value(eventRequest.getName()));

    }

    private EventRequest createNewEvent() {
        return EventRequest.builder()
                .name("test name")
                .alocatedSpots(0)
                .maximunSpots(1)
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
