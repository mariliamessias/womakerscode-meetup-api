package com.womakerscode.meetup.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.meetup.data.UserDetail;
import com.womakerscode.meetup.model.UserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtFilterAuthentication extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public static final int TOKEN_EXPIRATION = 600_000;
    public static final String TOKEN_PASSWORD = "9ebf2b42-3a5a-4193-ac50-73ea5548af28";

    public JwtFilterAuthentication(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserRequest user = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUserName(), user.getPassword(), new ArrayList<>()
            ));

        } catch (IOException e) {
            throw new RuntimeException("Fail to authenticate user", e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetail userDetail = (UserDetail) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(userDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(TOKEN_PASSWORD));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
