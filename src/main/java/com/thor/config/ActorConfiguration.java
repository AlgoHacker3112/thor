package com.thor.config;

import java.util.Map;
import lombok.Data;

@Data
public class ActorConfiguration {
    private String exchange;
    private String routingKey;
    private String queue;
    private boolean durable;
    private boolean autoDelete;
    private Map<String, Object> arguments;
    private String exchangeType;
}
