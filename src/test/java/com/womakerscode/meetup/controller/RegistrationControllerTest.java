package com.womakerscode.meetup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.exceptions.BusinessException;
import com.womakerscode.meetup.exceptions.ResourceNotFoundException;
import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.RegistrationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.womakerscode.meetup.model.entity.Status.ACTIVE;
import static com.womakerscode.meetup.model.entity.Status.CANCELED;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {RegistrationContoller.class})
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

    static String REGISTRATION_API = "/registrations";

    @MockBean
    RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Should create a registration with success")
    public void createRegistrationTest() throws Exception {

        //cenário
        RegistrationRequest registrationRequestBuilder = createNewRegistration();
        Registration savedRegistration = Registration.builder()
                .status(ACTIVE)
                .description("test")
                .event(Event.builder().name("event name").build())
                .username("test")
                .build();

        // execução
        BDDMockito.given(registrationService.save(any(RegistrationRequest.class))).willReturn(savedRegistration);

        String json = objectMapper.writeValueAsString(registrationRequestBuilder);

        MockHttpServletRequestBuilder request = post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("status").value("ACTIVE"))
                .andExpect(jsonPath("description").value(registrationRequestBuilder.getDescription()));

    }

    @Test
    @DisplayName("Should throw an exception when not have date enough for the test.")
    public void createInvalidRegistrationTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new RegistrationRequest());

        MockHttpServletRequestBuilder request = post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should throw an exception when try to create a new registration with an registration already created.")
    public void createRegistrationWithDuplicatedRegistration() throws Exception {

        RegistrationRequest registrationRequest = createNewRegistration();
        String json = new ObjectMapper().writeValueAsString(registrationRequest);

        BDDMockito.given(registrationService.save(any(RegistrationRequest.class)))
                .willThrow(new BusinessException("Registration already created!"));

        MockHttpServletRequestBuilder request = post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Registration already created!"));
    }

    @Test
    @DisplayName("Should get registration informations")
    public void getRegistrationTest() throws Exception {

        Long id = 11L;

        Registration registration = Registration.builder()
                .id(id)
                .description(createNewRegistration().getDescription())
                .status(ACTIVE)
                .event(Event.builder().name("event name").build())
                .build();

        BDDMockito.given(registrationService.getRegistrationById(id)).willReturn(Optional.of(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("description").value(createNewRegistration().getDescription()))
                .andExpect(jsonPath("status").value("ACTIVE"));

    }

    @Test
    @DisplayName("Should get registrations by username")
    public void getRegistrationByUserNameTest() throws Exception {

        Long id = 11L;

        Registration registration = Registration.builder()
                .id(id)
                .description(createNewRegistration().getDescription())
                .status(ACTIVE)
                .username("username")
                .event(Event.builder().name("event name").build())
                .build();

        BDDMockito.given(registrationService.getRegistrationByUserName("username")).willReturn(Collections.singletonList(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/users"))
                .param("user_name", "username")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*.id").value(11))
                .andExpect(jsonPath("*.description").value(createNewRegistration().getDescription()))
                .andExpect(jsonPath("*.status").value("ACTIVE"));

    }

    @Test
    @DisplayName("Should return NOT FOUND when the registration doesn't exists")
    public void registrationNotFoundTest() throws Exception {

        BDDMockito.given(registrationService.getRegistrationById(anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete the registration")
    public void deleteRegistration() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return resource bad request when no registration is not found to delete")
    public void deleteNonExistentRegistrationTest() throws Exception {

        BDDMockito.doThrow(new BusinessException("Fail to delete registration with id: 1")).when(registrationService).delete(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(REGISTRATION_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update registration")
    public void updateRegistrationTest() throws Exception {

        Long id = 11L;

        RegistrationRequest request = createNewRegistration();
        request.setStatus(CANCELED);

        String json = new ObjectMapper().writeValueAsString(request);

        Registration updatedRegistration =
                Registration.builder()
                        .id(id)
                        .description("test")
                        .status(CANCELED)
                        .event(Event.builder().name("event name").build())
                        .build();

        BDDMockito.given(registrationService
                .update(any(RegistrationRequest.class), eq(id)))
                .willReturn(updatedRegistration);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(REGISTRATION_API.concat("/" + id))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("status").value("CANCELED"))
                .andExpect(jsonPath("description").value("test"));

    }

    @Test
    @DisplayName("Should return 404 when try to update an registration no existent")
    public void updateNonExistentRegistrationTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(createNewRegistration());

        BDDMockito.doThrow(new ResourceNotFoundException("Registration id: 1 not found"))
                .when(registrationService).update(any(RegistrationRequest.class), anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(REGISTRATION_API.concat("/" + 1))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter registration")
    public void findRegistrationTest() throws Exception {

        Long id = 11L;

        Registration registration = Registration.builder()
                .id(id)
                .description("test")
                .status(ACTIVE)
                .event(Event.builder().name("event name").build())
                .build();

        BDDMockito.given(registrationService.find(Mockito.any(Registration.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Registration>(Arrays.asList(registration), PageRequest.of(0, 100), 1));

        String queryString = "?page=0&size=100";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));

    }

    private RegistrationRequest createNewRegistration() {
        return RegistrationRequest.builder()
                .username("user")
                .eventId(1L)
                .description("test")
                .build();
    }
}
