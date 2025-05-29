package com.thor.rmq;

public enum ActionType {
    TXN,
    TXN_REPLY;

    public final static String TXN_TEXT = "TXN";
    public final static String TXN_REPLY_TEXT = "TXN_REPLY";
}
