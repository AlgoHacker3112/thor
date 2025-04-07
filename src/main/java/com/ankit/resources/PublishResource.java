package com.ankit.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.ankit.core.PublishService;
import com.ankit.models.GenericResponse;
import com.ankit.rmq.message.TxnMessage;
import com.ankit.rmq.message.TxnReplyMessage;
import com.ankit.rmq.message.BaseRmqMessage;
import com.ankit.rmq.ActionType;
import com.google.inject.Inject;

@Path("/publish")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Publish Resource", description = "Publish Resource")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PublishResource {

    private final PublishService publishService;

    @POST
    @Operation(summary = "Publish Message", description = "Publish custom message to RMQ/MQTT")
    @ApiResponse(responseCode = "200", description = "Message Published")
    public GenericResponse<Object> publishMessage(BaseRmqMessage message) {
        publishService.publish(message);

        if (message instanceof TxnMessage) {
            TxnMessage txnMessage = (TxnMessage) message;
            return GenericResponse.builder()
                .statusCode(200)
                .data(
                    Json.mapper().createObjectNode()
                        .put("message", txnMessage.getMessage())
                        .put("transactionId", txnMessage.getTxnId())
                        .put("transactionType", txnMessage.getTxnType())
                        .put("transactionAmount", txnMessage.getTxnAmount())
                )
                .build();
        } else if (message instanceof TxnReplyMessage) {
            TxnReplyMessage txnReplyMessage = (TxnReplyMessage) message;
            return GenericResponse.builder()
                .statusCode(200)
                .data(
                    Json.mapper().createObjectNode()
                        .put("message", txnReplyMessage.getTxnMessage())
                        .put("transactionId", txnReplyMessage.getTxnId())
                        .put("transactionType", txnReplyMessage.getTxnType())
                        .put("transactionAmount", txnReplyMessage.getTxnAmount())
                        .put("transactionStatus", txnReplyMessage.getTxnStatus())
                )
                .build();
        }

        return GenericResponse.builder()
            .statusCode(400)
            .errorMessage("Unsupported message type")
            .build();
    }
}