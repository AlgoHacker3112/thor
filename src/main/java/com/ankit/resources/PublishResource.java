package com.ankit.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.ankit.models.GenericResponse;


@Path("/publish")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Publish Resource", description = "Publish Resource")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "BearerAuth")
public class PublishResource {

    public PublishResource() {
        // Constructor - dependencies injected later
    }

    @GET
    @Operation(summary = "Publish Message", description = "Publish Message to RMQ/MQTT")
    @ApiResponse(responseCode = "200", description = "Message Published")
    public GenericResponse<Object> publishMessage() {
        // TODO: Add logic to publish to RMQ/MQTT later
        return GenericResponse.builder()
            .statusCode(200)
            .data(Json.mapper().createObjectNode().put("message", "Message Published"))
            .build();
    }
}