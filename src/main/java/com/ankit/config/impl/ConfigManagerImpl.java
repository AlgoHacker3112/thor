package com.ankit.config.impl;

import com.ankit.config.ConfigManager;
import com.ankit.config.RmqConfiguration;
import com.ankit.rmq.ActionType;
import com.google.inject.Inject;
import com.ankit.config.ActorConfiguration;
import java.util.Map;
import com.ankit.AppConfiguration;
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
