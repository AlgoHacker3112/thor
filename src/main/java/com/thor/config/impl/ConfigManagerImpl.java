package com.thor.config.impl;

import com.google.inject.Inject;
import com.thor.AppConfiguration;
import com.thor.config.ActorConfiguration;
import com.thor.config.ConfigManager;
import com.thor.config.RmqConfiguration;
import com.thor.rmq.ActionType;

import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConfigManagerImpl implements ConfigManager {
    
    private final AppConfiguration appConfiguration;

    @Override
    public RmqConfiguration getRmqConfiguration() {
        return appConfiguration.getRmqConfiguration();
    }

    @Override
    public Map<ActionType, ActorConfiguration> getActorsConfig() {  
        return appConfiguration.getActorsConfig();
    }

    @Override
    public ActorConfiguration getActorConfiguration(final ActionType actionType) {
        return appConfiguration.getActorsConfig().get(actionType);
    }
}
