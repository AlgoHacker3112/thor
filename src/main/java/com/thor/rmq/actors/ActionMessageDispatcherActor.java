package com.thor.rmq.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.google.inject.Inject;
import com.thor.rmq.ActionMessageHandlerActor;
import com.thor.rmq.ActionType;
import com.thor.rmq.MessageHandler;
import com.thor.rmq.message.BaseRmqMessage;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ActionMessageDispatcherActor extends AbstractActor {

    Map<ActionType, ActorRef> actionMessagePublisherMap = new HashMap<>();

    private final Map<ActionType, MessageHandler< ? extends BaseRmqMessage>> handlers;

    public static Props props(Map<ActionType, MessageHandler< ? extends BaseRmqMessage>> handlers) {
        return Props.create(ActionMessageDispatcherActor.class, () -> new ActionMessageDispatcherActor(handlers));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(BaseRmqMessage.class, message -> {

                ActionType type = message.getActionType();

                log.info("Received message: {} of type: {}", message, type);

                ActorRef handler = actionMessagePublisherMap.computeIfAbsent(type, at -> {

                    log.info("Creating new handler for action type: {}", at);
                    MessageHandler< ? extends BaseRmqMessage> messageHandler = this.handlers.get(at);

                    log.debug("Message handler: {}", messageHandler);
                    log.debug("Boolean vlaue : {}", messageHandler.getMessageType().isAssignableFrom(message.getClass()));
                    log.debug("Message type: {}", message.getClass());
                    log.debug("Message handler type: {}", messageHandler.getMessageType());

                    if( messageHandler != null) {
                        return getContext().actorOf(ActionMessageHandlerActor.props(at, messageHandler), "handler-" + at.name());
                    } else{
                        log.error("No handler found for action type: {}", at);
                    }

                    return null;
                });
                

                
                if (handler != null) {
                    log.info("Sending message to handler: {}", handler);
                    handler.tell(message, getSelf());
                } else {
                    log.warn("Message of type {} dropped due to missing handler", message.getClass().getSimpleName());
                }

            })
            .build();
    }
}
