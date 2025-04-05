package com.ankit.rmq;

import akka.actor.ActorRef;
import com.ankit.config.ConfigManager;
import com.ankit.config.ActorConfiguration;
import com.ankit.managed.RmqManager;
import com.ankit.rmq.message.BaseRmqMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.dropwizard.lifecycle.Managed;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import akka.actor.ActorSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ru.vyarus.dropwizard.guice.module.installer.order.Order;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Order(20)
public class RmqConsumerManager implements Managed {

    private final RmqManager rmqManager;
    private final ConfigManager configManager; // Assuming ActorSystem is managed and injected
    private final ActorRef actionMessageDispatcherActor; // Inject the dispatcher actor ref
    private final ActorSystem actorSystem;

    private Channel channel;
    private final List<String> consumerTags = new ArrayList<>();
    private final ObjectMapper objectMapper = Json.mapper(); // Use the same mapper as publisher

    @Override
    public void start() throws Exception {
        log.info("Starting RmqConsumerManager...");
        try {
            channel = rmqManager.getConnection().createChannel();
            log.info("RMQ Channel created for consuming.");

            // Assuming ActionType is an enum and we can get all values
            for (ActionType actionType : ActionType.values()) {
                try {
                    val actorConfig = configManager.getActorConfiguration(actionType);
                    if (actorConfig != null) {
                        setupConsumerForType(channel, actorConfig, actionType);
                    } else {
                        log.warn("No RMQ configuration found for ActionType: {}", actionType);
                    }
                } catch (Exception e) {
                    log.error("Failed to set up consumer for ActionType: {}", actionType, e);
                    // Decide if we should continue starting other consumers or re-throw
                }
            }
            log.info("RmqConsumerManager started successfully.");
        } catch (IOException e) {
            log.error("Failed to start RmqConsumerManager", e);
            // Clean up channel if necessary
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException ce) {
                    log.error("Failed to close channel during startup failure", ce);
                }
            }
            throw e; // Re-throw to signal failure to Dropwizard
        }
    }

    private void setupConsumerForType(Channel consumerChannel, ActorConfiguration actorConfig, ActionType actionType) throws IOException {
        log.info("Setting up consumer for ActionType: {}, Queue: {}, Exchange: {}",
                actionType, actorConfig.getQueue(), actorConfig.getExchange());

        // Declare exchange (idempotent)
        consumerChannel.exchangeDeclare(
            actorConfig.getExchange(),
            actorConfig.getExchangeType(),
            actorConfig.isDurable()
        );

        // Declare queue (idempotent)
        consumerChannel.queueDeclare(
            actorConfig.getQueue(),
            actorConfig.isDurable(),
            false, // exclusive = false (allow multiple consumers if needed later)
            actorConfig.isAutoDelete(),
            actorConfig.getArguments()
        );

        // Bind queue to exchange
        consumerChannel.queueBind(
            actorConfig.getQueue(),
            actorConfig.getExchange(),
            actorConfig.getRoutingKey() // Use the same routing key as publisher
        );

        // Create the consumer callback
        DefaultConsumer consumer = new DefaultConsumer(consumerChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String messageBody = new String(body, "UTF-8");
                log.debug("Received raw message on queue [{}]: {}", actorConfig.getQueue(), messageBody);
                try {
                    // Deserialize message
                    BaseRmqMessage message = objectMapper.readValue(messageBody, BaseRmqMessage.class);

                    // Basic validation - ensure action type matches queue's intended type
                    if (message.getActionType() != actionType) {
                         log.warn("Received message with type {} on queue configured for type {}. Routing key: {}. Discarding.",
                                 message.getActionType(), actionType, envelope.getRoutingKey());
                         // Acknowledge message even if discarded to prevent redelivery loop
                         consumerChannel.basicAck(envelope.getDeliveryTag(), false);
                         return;
                    }


                    log.info("Successfully deserialized message: {}. Forwarding to dispatcher: {}", message, actionMessageDispatcherActor);

                    // Send to the dispatcher actor
                    actionMessageDispatcherActor.tell(message, ActorRef.noSender());

                    // Acknowledge the message
                    consumerChannel.basicAck(envelope.getDeliveryTag(), false);

                } catch (Exception e) {
                    log.error("Failed to process message from queue [{}]. Raw message: {}. Error: ", actorConfig.getQueue(), messageBody, e);
                    // Decide on error strategy: Nack and requeue? Nack and discard? Dead-letter?
                    // For now, Nack and discard to prevent potential poison pill loops
                    try {
                        consumerChannel.basicNack(envelope.getDeliveryTag(), false, false); // Do not requeue
                    } catch (IOException nackEx) {
                        log.error("Failed to NACK message", nackEx);
                    }
                }
            }
        };

        // Start consuming
        String consumerTag = consumerChannel.basicConsume(actorConfig.getQueue(), false, consumer); // autoAck = false
        consumerTags.add(consumerTag); // Store consumer tag for cleanup
        log.info("Consumer started for Queue: {} with tag: {}", actorConfig.getQueue(), consumerTag);
    }


    @Override
    public void stop() throws Exception {
        log.info("Stopping RmqConsumerManager...");
        try {
            if (channel != null && channel.isOpen()) {
                // Cancel all consumers
                for (String consumerTag : consumerTags) {
                    try {
                         log.debug("Cancelling consumer with tag: {}", consumerTag);
                        channel.basicCancel(consumerTag);
                    } catch (IOException e) {
                        log.error("Failed to cancel consumer tag: {}", consumerTag, e);
                    }
                }
                consumerTags.clear();
                log.info("Closing RMQ Channel.");
                channel.close();
            }
        } catch (IOException e) {
            log.error("Error stopping RmqConsumerManager", e);
            throw e; // Propagate exception
        }
        log.info("RmqConsumerManager stopped.");
    }
} 