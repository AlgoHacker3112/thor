package com.ankit.rmq.handlers;

import com.ankit.rmq.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import com.ankit.rmq.message.TxnMessage;
import lombok.RequiredArgsConstructor;
import com.google.inject.Inject;

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