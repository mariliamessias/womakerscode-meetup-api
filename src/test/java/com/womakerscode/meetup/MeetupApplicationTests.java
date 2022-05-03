package com.womakerscode.meetup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@PropertySource("classpath:application-test.yml")
class MeetupApplicationTests {

	@Test
	void contextLoads() {
	}

}
