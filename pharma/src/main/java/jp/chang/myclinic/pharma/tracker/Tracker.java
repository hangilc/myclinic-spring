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

    private String wsUrl;
    private WebsocketClient websocketClient;
    private ObjectMapper mapper = new ObjectMapper();
    private Dispatcher dispatcher;

    public Tracker(String wsUrl, DispatchHook hook, Service.ServerAPI service) {
        this.wsUrl = wsUrl;
        ModelRegistry registry = new ModelRegistry();
        ActionHook actionHook = new ActionHook(registry, hook);
        this.dispatcher = new Dispatcher(actionHook, service){
            @Override
            protected void beforeCatchup() {
                Tracker.this.beforeCatchup();
            }

            @Override
            protected void afterCatchup() {
                Tracker.this.afterCatchup();
            }
        };
    }

    public void start(){
        Thread thread = new Thread(dispatcher);
        thread.setDaemon(true);
        thread.start();
        startWebSocket();
    }

    public void shutdown(){
        if( websocketClient != null ) {
            websocketClient.shutdown();
        }
    }

    public void reload(){
        websocketClient.sendMessage("hello");
    }

    protected void beforeCatchup(){

    }

    protected void afterCatchup(){

    }

    private void startWebSocket(){
        this.websocketClient = new WebsocketClient(wsUrl){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("hello");
            }

            @Override
            protected void onNewMessage(String text){
                try {
                    System.out.println("message: " + text);
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
