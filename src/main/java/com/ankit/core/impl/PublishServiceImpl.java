package com.ankit.core.impl;

import com.ankit.core.PublishService;
import com.ankit.rmq.ActionMessagePublisher;
import com.ankit.rmq.message.BaseRmqMessage;

import lombok.RequiredArgsConstructor;
import com.google.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PublishServiceImpl implements PublishService {

    private final ActionMessagePublisher actionMessagePublisher;

    @Override
    public void publish(BaseRmqMessage message) {
        actionMessagePublisher.publish(message.getActionType(), message);
    }   
}
