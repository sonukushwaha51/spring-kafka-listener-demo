package com.spring.kafka.listener.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    private String eventId;

    private Date timestamp;

    private UUID eventUuid;

    private Map<String, Object> attributes;

}
