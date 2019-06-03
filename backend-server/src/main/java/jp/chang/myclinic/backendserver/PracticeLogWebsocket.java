package jp.chang.myclinic.backendserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PracticeLogWebsocket extends WebSocketAdapter{

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<>());

    public static void broadcast(String message){
        for(Session sess: peers.toArray(new Session[]{})){
            if( sess != null && sess.isOpen() ){
                sess.getRemote().sendStringByFuture(message);
            }
        }
    }

    private Session session;

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        peers.remove(session);
        this.session = null;
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        System.err.println("onWebSocketConnect: " + session);
        super.onWebSocketConnect(sess);
        this.session = sess;
        peers.add(sess);
        System.err.println("onWebSocketConnect (done): " + session);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        System.err.println("onWebSocketError");
        cause.printStackTrace();
        peers.remove(session);
        this.session = null;
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        if( "hello".equals(message) ){
            if( session != null && session.isOpen() ){
                List<PracticeLogDTO> logs = Context.listAllPracticeLog.apply(LocalDate.now());
                try {
                    String json = Context.mapper.writeValueAsString(logs);
                    session.getRemote().sendString(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
