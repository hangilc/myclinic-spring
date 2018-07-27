package jp.chang.myclinic.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);
    private Dispatcher dispatcher;
    private Thread dispatcherThread;
    private TrackerWebsocket websocket;
    private ObjectMapper mapper;

    public Tracker(String url, DispatchAction dispatchAction, ListLogFunction listLogFunction) {
        this.mapper = new ObjectMapper();
        this.dispatcher = new Dispatcher(dispatchAction, listLogFunction){
            @Override
            void beforeCatchup() {
                Tracker.this.beforeCatchup();
            }

            @Override
            void afterCatchup() {
                Tracker.this.afterCatchup();
            }
        };
        this.websocket = new TrackerWebsocket(url, message -> {
            try {
                PracticeLogDTO plog = mapper.readValue(message, PracticeLogDTO.class);
                if( dispatcher != null ){
                    dispatcher.add(plog);
                }
            } catch (IOException e) {
                logger.error("Cannot parse practice log.", e);
            }

        });
    }

    public void setCallbackWrapper(Consumer<Runnable> callbackWrapper){
        dispatcher.setCallbackWrapper(callbackWrapper);
    }

    public void start(Runnable cb){
        if( dispatcherThread == null ){
            this.dispatcherThread = new Thread(dispatcher);
            dispatcherThread.setDaemon(true);
            dispatcherThread.start();
        }
        websocket.start(cb);
    }

    public void stop(){
        websocket.stop();
    }

    public void shutdown(){
        websocket.shutdown();
        if( dispatcherThread != null ) {
            dispatcherThread.interrupt();
        }
    }

    public void probeUpdate(){
        websocket.probeUpdate();
    }

    public boolean isConnected(){
        return websocket.isConnected();
    }

    protected void beforeCatchup(){

    }

    protected void afterCatchup(){

    }

}
