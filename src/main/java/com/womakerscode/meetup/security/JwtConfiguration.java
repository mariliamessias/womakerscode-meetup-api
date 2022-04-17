package com.womakerscode.meetup.security;

import com.womakerscode.meetup.service.impl.UserDetailServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class JwtConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;

    public JwtConfiguration(UserDetailServiceImpl userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login", "/user", "/person").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtFilterAuthentication(authenticationManager()))
                .addFilter(new JwtFilterValidate(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
