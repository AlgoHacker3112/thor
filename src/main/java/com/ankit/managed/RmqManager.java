package com.ankit.managed;

import com.ankit.config.RmqConfiguration;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.lifecycle.Managed;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.vyarus.dropwizard.guice.module.installer.order.Order;

import com.google.inject.Inject;
@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Order(10)
public class RmqManager implements Managed{

    private final ConnectionFactory connectionFactory;
    private Connection connection;
    
    public RmqManager(RmqConfiguration configuration){
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(configuration.getHost());
        this.connectionFactory.setPort(configuration.getPort());
        this.connectionFactory.setUsername(configuration.getUsername());
        this.connectionFactory.setPassword(configuration.getPassword());
        this.connectionFactory.setVirtualHost(configuration.getVirtualHost());

    }

    @Override
    public void start() throws Exception {
        connection = connectionFactory.newConnection();
        System.out.println("RmqManager started");
    }

    @Override
    public void stop() throws Exception {
        connection.close();
        System.out.println("RmqManager stopped");
    }
    
    
}
