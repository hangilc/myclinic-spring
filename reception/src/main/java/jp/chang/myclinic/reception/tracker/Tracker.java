package jp.chang.myclinic.reception.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.reception.tracker.model.Wqueue;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);

    private String wsUrl;
    private WebsocketClient websocketClient;
    private ObjectMapper mapper = new ObjectMapper();
    private Dispatcher dispatcher;
    private ModelRegistry registry;

    public Tracker(String wsUrl, DispatchHook hook, Service.ServerAPI service) {
        this.wsUrl = wsUrl;
        registry = new ModelRegistry();
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

    public void start(Runnable openCallback){
        Thread thread = new Thread(dispatcher);
        thread.setDaemon(true);
        thread.start();
        startWebSocket(openCallback);
    }

    public void restart(Runnable openCallback){
        shutdown();
        startWebSocket(openCallback);
    }

    public boolean isRunning(){
        return websocketClient != null;
    }

    public void shutdown(){
        if( websocketClient != null ) {
            websocketClient.shutdown();
            websocketClient = null;
        }
    }

    public void reload(){
        websocketClient.sendMessage("hello");
    }

    protected void beforeCatchup(){

    }

    protected void afterCatchup(){

    }

    private void startWebSocket(Runnable openCallback){
        this.websocketClient = new WebsocketClient(wsUrl){
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                openCallback.run();
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

    public List<Wqueue> getWqueueList(){
        return registry.getWqueueList();
    }
}
