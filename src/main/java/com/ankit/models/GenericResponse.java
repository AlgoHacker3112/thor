package com.ankit.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jersey.errors.ErrorMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse<T> {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("errorDetails")
    private String errorDetails;

    @JsonProperty("data")
    private T data;
}
