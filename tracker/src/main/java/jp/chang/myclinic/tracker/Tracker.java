package jp.chang.myclinic.tracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tracker {

    private static Logger logger = LoggerFactory.getLogger(Tracker.class);
    private TrackerWebsocket websocket;

    public Tracker(String url) {
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
    }

    public boolean isConnected(){
        return websocket.isConnected();
    }

}
