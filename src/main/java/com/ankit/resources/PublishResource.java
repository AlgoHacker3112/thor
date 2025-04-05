package com.ankit.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.ankit.core.PublishService;
import com.ankit.models.GenericResponse;
import com.ankit.rmq.message.TxnMessage;
import com.google.inject.Inject;

@Path("/publish")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Publish Resource", description = "Publish Resource")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PublishResource {

    private final PublishService publishService;

    @GET
    @Operation(summary = "Publish Message", description = "Publish Message to RMQ/MQTT")
    @ApiResponse(responseCode = "200", description = "Message Published")
    public GenericResponse<Object> publishMessage() {
        

        publishService.publish(
                    TxnMessage.builder()
                        .txnId("123")
                        .build()
        );

        return GenericResponse.builder()
            .statusCode(200)
            .data(
                Json.mapper().createObjectNode().put("message", "Message Published")
            )
            .build();
    }
}