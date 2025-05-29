package com.thor.core.impl;

import lombok.RequiredArgsConstructor;
import com.google.inject.Inject;
import com.thor.core.PublishService;
import com.thor.rmq.ActionMessagePublisher;
import com.thor.rmq.message.BaseRmqMessage;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PublishServiceImpl implements PublishService {

    private final ActionMessagePublisher actionMessagePublisher;

    @Override
    public void publish(BaseRmqMessage message) {
        actionMessagePublisher.publish(message.getActionType(), message);
    }   
}
