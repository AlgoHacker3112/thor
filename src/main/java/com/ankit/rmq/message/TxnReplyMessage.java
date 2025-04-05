package com.ankit.rmq.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ankit.rmq.ActionType;
import lombok.Builder;


@Data
@EqualsAndHashCode(callSuper = true)

public class TxnReplyMessage extends BaseRmqMessage {
    
    private String txnId;
    private String txnType;
    private String txnAmount;
    private String txnStatus;
    private String txnMessage;

    public TxnReplyMessage() {
        super(ActionType.TXN_REPLY);
    }

    @Builder
    public TxnReplyMessage(
        final String txnId, 
        final String txnType, 
        final String txnAmount, 
        final String txnStatus, 
        final String txnMessage
    ) {
        this();
        this.txnId = txnId;
        this.txnType = txnType;
        this.txnAmount = txnAmount;
        this.txnStatus = txnStatus;
        this.txnMessage = txnMessage;
    }
    
}