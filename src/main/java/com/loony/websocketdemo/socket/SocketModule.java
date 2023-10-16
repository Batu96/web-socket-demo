package com.loony.websocketdemo.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.loony.websocketdemo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j // lombok annotation for logging
public class SocketModule {
    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketModule(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        // this method will be called when socket connection start
        // register event listener
        socketIOServer.addConnectListener(listener -> {// this method takes a parameter of type SocketIOClient
            log.info(String.format("SocketID: %s connected", listener.getSessionId().toString()));
        });
        // this method will be called when socket connection end
        socketIOServer.addDisconnectListener(listener -> {
            log.info(String.format("SocketID: %s disconnected", listener.getSessionId().toString()));
        });
        // this method will be called when socket connections send message is triggered
        // custom event listener / String.class is the type of data that will be received and, it can be any type
        //server(us) listening to event send-message from client
        socketIOServer.addEventListener("send-message", Message.class,(client, data, ackSender) -> {
            log.info(String.format("SocketID: %s send message: %s", client.getSessionId().toString(), data.getContent()));
            // send receive-event to all connected clients
            // getNamespace() define the namespace of the socket connection
            // client listening to event receive-message from server
            client.getNamespace().getBroadcastOperations().sendEvent("receive-message", data.getContent()
                    +" hi from server");
            });
    }
}
