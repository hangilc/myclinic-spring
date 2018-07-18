package jp.chang.myclinic.tracker.main;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track {

    private static Logger logger = LoggerFactory.getLogger(Track.class);

    public static void main(String[] args){
        Service.setServerUrl("http://localhost:18080/json");
        Tracker tracker = new Tracker("http://localhost:18080/practice-log", Service.api);
        tracker.start(() -> {
        });
        try {
            Thread.sleep(3000);
            tracker.shutdown();
            Service.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
