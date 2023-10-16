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
        socketIOServer.addConnectListener(client -> {// this method takes a parameter of type SocketIOClient
            log.info(String.format("SocketID: %s connected", client.getSessionId().toString()));
            // get room parameter from url
            String room = client.getHandshakeData().getSingleUrlParam("room");
            // join room
            client.joinRoom(room);
        });
        // this method will be called when socket connection end
        socketIOServer.addDisconnectListener(listener -> {
            log.info(String.format("SocketID: %s disconnected", listener.getSessionId().toString()));
        });
        // this method will be called when socket connections send message is triggered
        // custom event listener / String.class is the type of data that will be received and, it can be any type
        //server(us) listening to event send-message from client
        socketIOServer.addEventListener("send-message", Message.class, (client, data, ackSender) -> {
            log.info(String.format("SocketID: %s send message: %s", client.getSessionId().toString(), data.getContent()));
           //get room parameter from url
           String room = client.getHandshakeData().getSingleUrlParam("room");
            // send receive-event to all connected clients
            // getNamespace() define the namespace of the socket connection
            // client listening to event receive-message from server
            // getRoomOperations(room) define the room of the client
            client.getNamespace().getRoomOperations(room).getClients().forEach(id -> {
                // send message to all connected clients except the sender
                if (!id.getSessionId().equals(client.getSessionId()))
                    id.sendEvent("receive-message", data.getContent());

            });
        });
    }
}
