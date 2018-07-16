package jp.chang.myclinic.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class PublishingWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(PublishingWebSocketHandler.class);

    private List<WebSocketSession> sessions = new ArrayList<>();
    private TextMessage lastMessage;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("websocket connected {}" + session);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message received: " + message.getPayload());
        if( lastMessage != null ){
            session.sendMessage(lastMessage);
        }
    }

    final public void publish(String value) throws Exception {
        TextMessage message = new TextMessage(value.getBytes(StandardCharsets.UTF_8));
        for (WebSocketSession sess : sessions) {
            sess.sendMessage(message);
        }
        this.lastMessage = message;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("websocket disconnected {}", session);
        sessions.remove(session);
    }

}
