package com.thor.rmq.handlers;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.google.inject.Inject;
import com.thor.rmq.MessageHandler;
import com.thor.rmq.message.TxnMessage;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TnxMessageHandler implements MessageHandler<TxnMessage> {

    @Override
    public void handle(TxnMessage message) {
        log.info("Handling tnx message: {}", message);  
    }

    @Override
    public Class<TxnMessage> getMessageType() {
        return TxnMessage.class;
    }
}