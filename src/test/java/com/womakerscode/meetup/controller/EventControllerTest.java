package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.configs.Properties;
import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.service.EventService;
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

import java.util.Optional;

import static com.womakerscode.meetup.model.entity.Status.ACTIVE;
import static com.womakerscode.meetup.model.entity.Status.CANCELED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

        // execução
        BDDMockito.given(eventService.save(any(EventRequest.class))).willReturn(event);

        String json = objectMapper.writeValueAsString(eventRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EVENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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

        // execução

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(EVENT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest());

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

        // execução
        BDDMockito.given(eventService.getEventById(id)).willReturn(Optional.of(event));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EVENT_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

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
        // execução
        BDDMockito.given(eventService.getEventById(id)).willReturn(Optional.empty());


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should delete an event by id with success")
    public void deleteEventByIdTest() throws Exception {

        long id = 1L;

        // execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isNoContent());

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

        String json = objectMapper.writeValueAsString(eventRequest);

        // execução
        BDDMockito.given(eventService.update(any(EventRequest.class), eq(id))).willReturn(event);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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

        String json = objectMapper.writeValueAsString(eventRequest);

        // execução
        BDDMockito.given(eventService.cancel(eq(id))).willReturn(event);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(EVENT_API.concat("/" + id + "/cancel"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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

    private EventRequest createNewEvent() {
        return EventRequest.builder()
                .name("test name")
                .alocatedSpots(0)
                .maximunSpots(1)
                .build();
    }
}
