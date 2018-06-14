package jp.chang.myclinic.server;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class PracticeLogHandler extends TextWebSocketHandler {

    //private static Logger logger = LoggerFactory.getLogger(PracticeLogHandler.class);

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connected " + session);
        sessions.add(session);
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//    }

    public void sendMessage(String value) throws Exception {
        for (WebSocketSession sess : sessions) {
            TextMessage message = new TextMessage(value.getBytes(StandardCharsets.UTF_8));
            sess.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("disconnected " + session);
        sessions.remove(session);
    }
}
