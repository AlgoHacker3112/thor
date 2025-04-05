package com.ankit.rmq.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Builder;

import com.ankit.rmq.ActionType;
@Data
@EqualsAndHashCode(callSuper = true)
public class TxnMessage extends BaseRmqMessage {

    private String txnId;
    private String txnType;
    private String txnAmount;

    public TxnMessage() {
        super(ActionType.TXN);
    }

    @Builder
    public TxnMessage(final String txnId, final String txnType, final String txnAmount) {
        this();
        this.txnId = txnId;
        this.txnType = txnType;
        this.txnAmount = txnAmount;
    }


}
