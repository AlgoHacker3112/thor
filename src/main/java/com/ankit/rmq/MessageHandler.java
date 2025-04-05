package com.ankit.rmq;

import com.ankit.rmq.message.BaseRmqMessage;

public interface MessageHandler<T extends BaseRmqMessage> {
    void handle(T message); 
    Class<T> getMessageType();
}
