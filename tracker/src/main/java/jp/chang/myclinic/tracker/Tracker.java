package jp.chang.myclinic.tracker;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);
    private Dispatcher dispatcher;
    private Thread dispatcherThread;
    private TrackerWebsocket websocket;

    public Tracker(String url, Service.ServerAPI clientAPI) {
        DispatchAction dispatchAction = new DispatchAction(){

        };
        this.dispatcher = new Dispatcher(dispatchAction, clientAPI::listPracticeLogInRangeCall);
        this.dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        this.websocket = new TrackerWebsocket(url, message -> {
            System.out.println(message);
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
