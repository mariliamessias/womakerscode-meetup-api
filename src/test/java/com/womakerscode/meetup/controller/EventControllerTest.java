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
import static com.womakerscode.meetup.model.entity.Status.CANCELED;
import static org.mockito.ArgumentMatchers.*;
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
                .maximunSpots(10)
                .alocatedSpots(1)
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
                .andExpect(jsonPath("maximun_spots").value("10"))
                .andExpect(jsonPath("alocated_spots").value("1"))
                .andExpect(jsonPath("name").value(eventRequest.getName()));

    }

    @Test
    @DisplayName("Should not create an event with success when payload is invalid")
    public void badRequestcreateEventTest() throws Exception {

        //cenário
        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EVENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should not create an event when has not Authorization token")
    public void forbiddenErrorCreateEventTest() throws Exception {

        //cenário
        EventRequest eventRequest = createNewEvent();
        // execução
        String json = objectMapper.writeValueAsString(eventRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EVENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should get an event by id with success")
    public void getEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        Event event = Event.builder()
                .id(1L)
                .maximunSpots(10)
                .alocatedSpots(1)
                .status(ACTIVE)
                .name("test name")
                .build();

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(eventService.getEventById(id)).willReturn(Optional.of(event));

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EVENT_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("maximun_spots").value("10"))
                .andExpect(jsonPath("alocated_spots").value("1"))
                .andExpect(jsonPath("name").value(event.getName()));

    }

    @Test
    @DisplayName("Should not get an event by id when not found event")
    public void notFoundErrorgetEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(eventService.getEventById(id)).willReturn(Optional.empty());

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should not get by id an event when has not Authorization token")
    public void forbiddenErrorGetEventByIdTest() throws Exception {

        long id = 1L;
        // execução

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EVENT_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should delete an event by id with success")
    public void deleteEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail));

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Should not delete an event by id when has not Authorization token")
    public void forbiddenErrorDeleteEventByIdTest() throws Exception {

        long id = 1L;
        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should update an event by id with success")
    public void updateEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        EventRequest eventRequest = createNewEvent();
        Event event = Event.builder()
                .status(ACTIVE)
                .maximunSpots(50)
                .alocatedSpots(1)
                .name("test name")
                .build();

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        String json = objectMapper.writeValueAsString(eventRequest);

        // execução
        BDDMockito.given(eventService.update(any(EventRequest.class), eq(id))).willReturn(event);
        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail))
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(jsonPath("maximun_spots").value("50"))
                .andExpect(jsonPath("alocated_spots").value("1"))
                .andExpect(jsonPath("name").value(eventRequest.getName()));

    }

    @Test
    @DisplayName("Should not update an event by id when has not Authorization token")
    public void forbiddenErrorUpdateEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        EventRequest eventRequest = createNewEvent();

        String json = objectMapper.writeValueAsString(eventRequest);

        // execução

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should update to canceled an event by id with success")
    public void updateCancelEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        EventRequest eventRequest = createNewEvent();
        Event event = Event.builder()
                .status(CANCELED)
                .maximunSpots(10)
                .alocatedSpots(1)
                .name("test name")
                .build();

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        String json = objectMapper.writeValueAsString(eventRequest);

        // execução
        BDDMockito.given(eventService.cancel(eq(id))).willReturn(event);
        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id + "/cancel"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + buildToken(userDetail))
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("CANCELED"))
                .andExpect(jsonPath("maximun_spots").value("10"))
                .andExpect(jsonPath("alocated_spots").value("1"))
                .andExpect(jsonPath("name").value(eventRequest.getName()));

    }

    @Test
    @DisplayName("Should not update to canceled an event by id when has not Authorization token")
    public void forbiddenErrorUpdateCancelEventByIdTest() throws Exception {

        long id = 1L;

        //cenário
        EventRequest eventRequest = createNewEvent();

        String json = objectMapper.writeValueAsString(eventRequest);

        // execução

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id + "/cancel"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isForbidden());

    }

    private String buildToken(UserDetail userDetail) {

        BDDMockito.given(properties.getProperty(anyString())).willReturn("9ebf2b42-3a5a-4193-ac50-73ea5547af27");

        return JWT.create()
                .withSubject(userDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(properties.getProperty("token.password")));
    }

    private EventRequest createNewEvent() {
        return EventRequest.builder()
                .name("test name")
                .alocatedSpots(0)
                .maximunSpots(1)
                .build();
    }
}
