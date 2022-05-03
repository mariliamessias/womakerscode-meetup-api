package com.womakerscode.meetup.service;

import com.womakerscode.meetup.configs.MQConfig;
import com.womakerscode.meetup.model.SendEmaillMessage;
import com.womakerscode.meetup.service.impl.PublisherServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PublisherServiceTest {

    @InjectMocks
    PublisherServiceImpl publisherService;

    @Mock
    private RabbitTemplate template;

    @Test
    @DisplayName("Should publish message")
    public void publishMessageTest() {

        SendEmaillMessage message = SendEmaillMessage.builder().build();

        assertDoesNotThrow(() -> publisherService.publish(message));
        //asserts
        verify(template, Mockito.times(1)).convertAndSend(eq(MQConfig.EXCHANGE),
                eq(MQConfig.ROUTING_KEY), eq(message));

    }

}
