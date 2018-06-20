package jp.chang.myclinic.pharma.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);

    private WebsocketClient websocketClient;
    private ObjectMapper mapper = new ObjectMapper();
    private Dispatcher dispatcher;

    public Tracker(String wsUrl, DispatchAction action, Service.ServerAPI service) {
        this.websocketClient = new WebsocketClient(wsUrl);
        this.dispatcher = new Dispatcher(action, service);
    }

    public void start(){

    }

    public void shutdown(){

    }

    private void startWebSocket(String wsUrl){
        websocketClient = new WebsocketClient(wsUrl){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("hello");
            }

            @Override
            protected void onNewMessage(String text){
                try {
                    PracticeLogDTO plog = mapper.readValue(text, PracticeLogDTO.class);
                    if( dispatcher != null ){
                        dispatcher.add(plog);
                    }
                } catch (IOException e) {
                    logger.error("Cannot parse practice log.", e);
                }
            }

        };
        logger.info("Started web socket");
    }

}
