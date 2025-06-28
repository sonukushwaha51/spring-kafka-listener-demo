package com.spring.kafka.listener.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaListenerConfig {

    @Value("${spring.kafka.consumer.group.id}")
    private String groupId;

    @Value("${spring.kafka.comsumer.client.id}")
    private String clientId;

    @Value("${spring.kafka.consumer.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.listener.concurrency}")
    private int concurrency;

    @Bean
    public ConsumerFactory<String, byte[]> kafkaConsumerFactory(Map<String,Object> kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties, new StringDeserializer(), new ByteArrayDeserializer());
    }

    @Bean
    public Map<String, Object> kafkaProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return properties;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> globalConcurrentContainerFactory() {
        return kafkaConcurrentContainerFactory(kafkaProperties());
    }

    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaConcurrentContainerFactory(Map<String,Object> kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(concurrency);
        factory.setConsumerFactory(kafkaConsumerFactory(kafkaProperties));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;

    }

}
