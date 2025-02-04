package com.eventgate.userservice.utils;

import com.eventgate.userservice.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class KafkaUtils {
    private final KafkaTemplate<String, UserResponse> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    public void sendMessage(UserResponse user, String token) {
        Message<UserResponse> message = MessageBuilder
                .withPayload(user)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, user.id())
                .setHeader("Authorization", "Bearer " + token)
                .build();

        kafkaTemplate.send(message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("User sent successfully: {}", user.id());
                    } else {
                        log.error("Failed to send user: {}", user.id());
                    }
                });
    }
}
