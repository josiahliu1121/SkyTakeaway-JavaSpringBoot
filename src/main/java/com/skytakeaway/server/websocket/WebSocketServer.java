package com.skytakeaway.server.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/admin/ws/{sid}")
public class WebSocketServer {
    //store connection
    private static Map<String, Session> sessionMap = new HashMap<>();

    //execute when client connect to server
    @OnOpen
    public void onOpen (Session session, @PathParam("sid") String sid){
        System.out.println("client:" + sid + "connected");
        sessionMap.put(sid, session);
    }

    //execute when client disconnect from server
    @OnClose
    public void onClose(@PathParam("sid") String sid){
        System.out.println("client:" + sid + "disconnected");
        sessionMap.remove(sid);
    }

    @OnMessage
    public void onMessage (String message, @PathParam("sid") String sid){
        System.out.println("receive message from client " + sid + ":" + message);
    }

    public void sendToAllClient (String message){
        Collection<Session> sessions = sessionMap.values();
        for(Session session : sessions){
            try {
                session.getBasicRemote().sendText(message);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
