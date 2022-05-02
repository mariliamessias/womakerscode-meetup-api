package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.configs.MQConfig;
import com.womakerscode.meetup.model.SendEmaillMessage;
import com.womakerscode.meetup.service.PublisherService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private RabbitTemplate template;

    @Override
    public void publish(SendEmaillMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        template.convertAndSend(MQConfig.EXCHANGE,
                MQConfig.ROUTING_KEY, message);

    }
}
