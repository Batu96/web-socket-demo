package com.loony.websocketdemo.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    //retrieve port from application.properties
    @Value("${socket-server.port}")
    private Integer port;
    //retrieve host from application.properties
    @Value("${socket-server.host}")
    private String host;
    //create bean for SocketIOServer
    // this method return SocketIOServer object from socketio
    @Bean
    public SocketIOServer socketIOServer() {
        //this config from socketio
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);

        return new SocketIOServer(config);
    }
}
