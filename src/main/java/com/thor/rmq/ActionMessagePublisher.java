package com.thor.rmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import com.google.inject.Inject;
import com.rabbitmq.client.Channel;
import com.thor.config.ConfigManager;
import com.thor.managed.RmqManager;
import com.thor.rmq.message.BaseRmqMessage;

import io.swagger.v3.core.util.Json;
import lombok.extern.slf4j.Slf4j;
@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class ActionMessagePublisher {
    
    private final RmqManager rmqManager;
    private final ConfigManager configManager;

    public void publish(final ActionType actionType, final BaseRmqMessage message) {

        try(Channel channel = rmqManager.getConnection().createChannel()) {

            val actorConfig = configManager.getActorConfiguration(actionType);

            log.info("Declaring exchange: {} for action type: {}", actorConfig, actionType);

            channel.exchangeDeclare(
                actorConfig.getExchange(), 
                actorConfig.getExchangeType(), 
                actorConfig.isDurable()
            );

            channel.queueDeclare(
                actorConfig.getQueue(),
                actorConfig.isDurable(),
                false,
                actorConfig.isAutoDelete(),
                actorConfig.getArguments()
            );

            // Bind queue to exchange
            channel.queueBind(
                actorConfig.getQueue(),
                actorConfig.getExchange(),
                actorConfig.getRoutingKey()
            );


            val routingKey = actorConfig.getRoutingKey();

            if(routingKey == null || routingKey.isEmpty()) {
                throw new IllegalArgumentException("Routing key is not set for action type: " + actionType);
            }

            val messageJson = Json.mapper().writeValueAsString(message);

            log.info("Publishing message: {} to exchange: {} with routing key: {}", messageJson, actorConfig.getExchange(), routingKey);

            channel.basicPublish(actorConfig.getExchange(), routingKey, null, messageJson.getBytes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
