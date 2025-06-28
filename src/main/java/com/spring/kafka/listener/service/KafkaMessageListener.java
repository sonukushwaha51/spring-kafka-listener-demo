package com.spring.kafka.listener.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.kafka.listener.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageListener {

    private static final String KAFKA_TOPIC = "parking-inbound-sync";

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = KAFKA_TOPIC, containerFactory = "globalConcurrentContainerFactory", concurrency = "5")
    public void onMessageReceived(String message,
                                  @Header(name = "eventUuid", required = false) String eventUuid) {

        try {
            //log.info("Message received {}", message);
            Event event = objectMapper.readValue(message, Event.class);
        } catch (JsonProcessingException exception) {
            //log.error("Error while parsing message {}", exception.getMessage());
        }

    }
}