package com.womakerscode.meetup.security;

import com.womakerscode.meetup.configs.Properties;
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
    private final Properties properties;

    public JwtConfiguration(UserDetailServiceImpl userDetailService, PasswordEncoder passwordEncoder, Properties properties) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
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
                .addFilter(new JwtFilterAuthentication(authenticationManager(), properties))
                .addFilter(new JwtFilterValidate(authenticationManager(), properties))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
