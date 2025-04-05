package com.ankit.rmq.message;

import com.ankit.rmq.ActionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "actionType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TxnMessage.class, name = ActionType.TXN_TEXT),
    @JsonSubTypes.Type(value = TxnReplyMessage.class, name = ActionType.TXN_REPLY_TEXT)
})
@NoArgsConstructor
@AllArgsConstructor
public class BaseRmqMessage {

    private ActionType actionType;
}
