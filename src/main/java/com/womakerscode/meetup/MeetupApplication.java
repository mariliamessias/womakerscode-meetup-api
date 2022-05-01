package com.womakerscode.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class MeetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetupApplication.class, args);
    }
}
