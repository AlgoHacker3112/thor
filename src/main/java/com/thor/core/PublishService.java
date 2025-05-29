package com.thor.core;

import com.thor.rmq.message.BaseRmqMessage;

public interface PublishService {
    void publish(BaseRmqMessage message);
}
