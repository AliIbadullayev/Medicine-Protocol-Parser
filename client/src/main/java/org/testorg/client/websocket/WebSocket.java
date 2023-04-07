package org.testorg.client.websocket;

import org.testorg.client.pojo.Message;
import org.testorg.client.util.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value="/websocket", encoders = MessageEncoder.class )
public class WebSocket {

    private Session session;
    private static final Set<WebSocket> chatEndpoints
            = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        chatEndpoints.add(this);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        chatEndpoints.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    public static void broadcast(Message message)
            throws IOException, EncodeException {

        for (WebSocket endpoint : chatEndpoints) {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().
                            sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
