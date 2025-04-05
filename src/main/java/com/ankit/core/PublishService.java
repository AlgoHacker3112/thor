package com.ankit.core;

import com.ankit.rmq.message.BaseRmqMessage;

public interface PublishService {
    void publish(BaseRmqMessage message);
}
