package com.womakerscode.meetup.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.configs.Properties;
import com.womakerscode.meetup.data.UserDetail;
import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.UserRequest;
import com.womakerscode.meetup.model.entity.User;
import com.womakerscode.meetup.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {UserController.class})
@AutoConfigureMockMvc
public class UserControllerTest {

    static String USER_API = "/user";

    @MockBean
    UserService userService;

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
        UserRequest userRequest = createNewUser();
        User user = User.builder()
                .userName("test username")
                .password("1234")
                .build();

        UserDetail userDetail = new UserDetail(Optional.of(User.builder().userName("test").build()));
        // execução
        BDDMockito.given(userService.save(any(UserRequest.class))).willReturn(user);

        BDDMockito.given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetail);

        String json = objectMapper.writeValueAsString(userRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(USER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // asserts
        mockMvc
                .perform(request)
                .andExpect(status().isCreated());

    }

    private UserRequest createNewUser() {
        return UserRequest.builder()
                .userName("test username")
                .password("1234")
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
