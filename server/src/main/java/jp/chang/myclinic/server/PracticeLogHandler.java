package jp.chang.myclinic.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Logger logger = LoggerFactory.getLogger(PracticeLogHandler.class);

    //@Autowired
    private DbGateway dbGateway;
    //@Autowired
    private ObjectMapper objectMapper;
    private List<WebSocketSession> sessions = new ArrayList<>();
    private TextMessage lastMessage;

    @Autowired
    public PracticeLogHandler(DbGateway dbGateway, ObjectMapper objectMapper) throws Exception {
        this.dbGateway = dbGateway;
        this.objectMapper = objectMapper;
        sendLastLog();
    }

    private void sendLastLog() throws Exception {
        PracticeLogDTO lastLog = dbGateway.findLastPracticeLog();
        logger.info("last practice log: {}", lastLog);
        if( lastLog != null ) {
            sendPracticeLogMessage(objectMapper.writeValueAsString(lastLog));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connected " + session);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if( lastMessage != null ){
            session.sendMessage(lastMessage);
            logger.info("sent text message: " + lastMessage);
        }
    }

    final public void sendPracticeLogMessage(String value) throws Exception {
        TextMessage message = new TextMessage(value.getBytes(StandardCharsets.UTF_8));
        for (WebSocketSession sess : sessions) {
            sess.sendMessage(message);
        }
        this.lastMessage = message;
        logger.info("set lastMessage: {}", message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("disconnected " + session);
        sessions.remove(session);
    }
}
