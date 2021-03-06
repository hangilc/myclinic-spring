package jp.chang.myclinic.hotline.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
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

    public Tracker(String wsUrl, DispatchAction action, Service.ServerAPI service) {
        this.wsUrl = wsUrl;
        this.dispatcher = new Dispatcher(action, service){
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

    protected void onError(String message){

    }

    protected void onOpen(){

    }

    private void startWebSocket(){
        this.websocketClient = new WebsocketClient(wsUrl){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                webSocket.send("hello");
                Tracker.this.onOpen();
            }

            @Override
            protected void onNewMessage(String text){
                try {
                    HotlineLogDTO log = mapper.readValue(text, HotlineLogDTO.class);
                    if( dispatcher != null ){
                        dispatcher.add(log);
                    }
                } catch (IOException e) {
                    logger.error("Cannot parse practice log.", e);
                }
            }

            @Override
            protected void onError(String message) {
                super.onError(message);
                Tracker.this.onError(message);
            }
        };
        logger.info("Started web socket");
    }

}
