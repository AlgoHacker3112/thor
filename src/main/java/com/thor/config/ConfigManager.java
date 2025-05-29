package com.thor.config;

import java.util.Map;

import com.thor.rmq.ActionType;

public interface ConfigManager {
    
    public RmqConfiguration getRmqConfiguration();

    public ActorConfiguration getActorConfiguration(final ActionType actionType);

    public Map<ActionType, ActorConfiguration> getActorsConfig();

}
