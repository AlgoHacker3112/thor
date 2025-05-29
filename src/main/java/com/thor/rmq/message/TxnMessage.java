package com.thor.rmq.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.thor.rmq.ActionType;

import lombok.Builder;
@Data
@EqualsAndHashCode(callSuper = true)
public class TxnMessage extends BaseRmqMessage {

    private String txnId;
    private String txnType;
    private String txnAmount;
    private String message;

    public TxnMessage() {
        super(ActionType.TXN);
    }

    @Builder
    public TxnMessage(final String txnId, final String txnType, final String txnAmount, final String message) {
        this();
        this.txnId = txnId;
        this.txnType = txnType;
        this.txnAmount = txnAmount;
        this.message = message;
    }


}
