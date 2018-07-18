package jp.chang.myclinic.tracker.main;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.tracker.Tracker;
import jp.chang.myclinic.tracker.model.ModelAction;
import jp.chang.myclinic.tracker.model.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track implements ModelAction {

    private static Logger logger = LoggerFactory.getLogger(Track.class);

    public static void main(String[] args){
        Service.setServerUrl("http://localhost:18080/json");
        Tracker tracker = new Tracker("http://localhost:18080/practice-log", Service.api, new Track());
        tracker.start(() -> {
        });
//        try {
//            Thread.sleep(3000);
//            tracker.shutdown();
//            Service.stop();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onVisitCreated(Visit visit, Runnable toNext) {
        System.out.println("visit creatd: " + visit);
        System.out.println("patient: " + visit.getPatient());
        toNext.run();
    }

    @Override
    public void onVisitDeleted(int visitId, Runnable toNext) {
        System.out.println("visit deleted: " + visitId);
        toNext.run();
    }
}
