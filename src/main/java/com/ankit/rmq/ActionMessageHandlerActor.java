package com.ankit.rmq;
import com.ankit.rmq.message.BaseRmqMessage;

import akka.actor.AbstractActor;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActionMessageHandlerActor extends AbstractActor {

    private final ActionType actionType;
    private final MessageHandler< ? extends BaseRmqMessage> messageHandler;

    public static Props props(ActionType actionType, MessageHandler< ? extends BaseRmqMessage> messageHandler) {
        return Props.create(ActionMessageHandlerActor.class, () -> new ActionMessageHandlerActor(actionType, messageHandler));
    }

    public ActionMessageHandlerActor(ActionType actionType, MessageHandler< ? extends BaseRmqMessage> messageHandler) {
        this.actionType = actionType;
        this.messageHandler = messageHandler;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(BaseRmqMessage.class, message -> {
                invokeHandlerSafely(message);
                log.info("Handler [{}] processing: {}", actionType, message);
            })
            .build();
    }

    @SuppressWarnings("unchecked")
    private void invokeHandlerSafely(BaseRmqMessage message) {
        ((MessageHandler<BaseRmqMessage>) messageHandler).handle(message);
    }
}
