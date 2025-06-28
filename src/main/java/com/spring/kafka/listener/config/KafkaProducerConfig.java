package com.spring.kafka.listener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.consumer.group.id}")
    private String groupId;

    @Value("${spring.kafka.comsumer.client.id}")
    private String clientId;

    @Value("${spring.kafka.consumer.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<Object, Object> kafkaProducerFactory(Map<String, Object> kafkaProducerProperties, ObjectMapper objectMapper) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(kafkaProducerProperties);
        producerFactory.setValueSerializer(jsonSerializer(objectMapper));
        return  producerFactory;
    }

    @Bean
    public TopicBuilder kafkaTopic() {
        return TopicBuilder.name("parking-inbound-sync");
    }

    @Bean
    public JsonSerializer<Object> jsonSerializer(ObjectMapper objectMapper) {
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper);
        jsonSerializer.setAddTypeInfo(false);
        return jsonSerializer;
    }

    @Bean
    public Map<String, Object> kafkaProducerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return properties;
    }

}
