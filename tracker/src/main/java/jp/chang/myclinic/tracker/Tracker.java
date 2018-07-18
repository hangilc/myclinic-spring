package jp.chang.myclinic.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.tracker.model.ModelAction;
import jp.chang.myclinic.tracker.model.ModelRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);
    private ModelRegistry modelRegistry;
    private Dispatcher dispatcher;
    private Thread dispatcherThread;
    private TrackerWebsocket websocket;
    private ObjectMapper mapper;

    public Tracker(String url, Service.ServerAPI clientAPI, ModelAction modelAction) {
        modelRegistry = new ModelRegistry(clientAPI);
        DispatchAction dispatchAction = new ModelDispatchAction(modelRegistry, modelAction);
        this.mapper = new ObjectMapper();
        this.dispatcher = new Dispatcher(dispatchAction, clientAPI::listPracticeLogInRangeCall);
        this.dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        this.websocket = new TrackerWebsocket(url, message -> {
            System.err.println("message: " + message);
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

    public void start(Runnable cb){
        websocket.start(cb);
    }

    public void stop(){
        websocket.stop();
    }

    public void shutdown(){
        websocket.shutdown();
        dispatcherThread.interrupt();
    }

    public boolean isConnected(){
        return websocket.isConnected();
    }

}
