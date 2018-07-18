package jp.chang.myclinic.tracker.main;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.tracker.DispatchAction;
import jp.chang.myclinic.tracker.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track implements DispatchAction {

    private static Logger logger = LoggerFactory.getLogger(Track.class);

    public static void main(String[] args){
        Service.setServerUrl("http://localhost:18080/json");
        Tracker tracker = new Tracker("http://localhost:18080/practice-log", new Track(),
                Service.api::listPracticeLogInRangeCall);
        tracker.start(() -> {
        });
    }

}
