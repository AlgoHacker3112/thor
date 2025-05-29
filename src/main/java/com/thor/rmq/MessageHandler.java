package com.thor.rmq;

import com.thor.rmq.message.BaseRmqMessage;

public interface MessageHandler<T extends BaseRmqMessage> {
    void handle(T message); 
    Class<T> getMessageType();
}
