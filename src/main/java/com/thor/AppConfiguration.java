package com.thor;

import io.dropwizard.Configuration;
import jakarta.validation.constraints.NotNull;

import java.beans.JavaBean;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thor.config.ActorConfiguration;
import com.thor.config.RmqConfiguration;
import com.thor.rmq.ActionType;

import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Data;

@Data
public class AppConfiguration extends Configuration {
    // TODO: implement service configuration

    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Thor";

    @NotNull
    @JsonProperty
    private SwaggerBundleConfiguration swagger;


    @NotNull
    @JsonProperty
    private RmqConfiguration rmqConfiguration;

    @NotNull
    @JsonProperty
    private Map<ActionType, ActorConfiguration> actorsConfig;

    // @Valid
    // @NotNull
    // private RabbitMQFactory rabbitmq = new RabbitMQFactory();

    // @JsonProperty("rabbitmq")
    // public void setRabbitMQFactory(RabbitMQFactory factory) { this.rabbitmq = factory; }
}
