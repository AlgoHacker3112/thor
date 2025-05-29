package com.thor.config;

import lombok.Data;

@Data
public class RmqConfiguration  {

    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;
    private String exchange;
    private String routingKey;
    
}
