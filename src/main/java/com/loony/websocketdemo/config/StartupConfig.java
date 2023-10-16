package com.loony.websocketdemo.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//this class for start socketio config when spring boot start
@Component
public class StartupConfig implements CommandLineRunner {
    @Autowired
    public StartupConfig(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    private final SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
    }
}
