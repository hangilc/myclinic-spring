package jp.chang.myclinic.tracker.main;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.tracker.Tracker;
import jp.chang.myclinic.tracker.model.ModelAction;
import jp.chang.myclinic.tracker.model.Text;
import jp.chang.myclinic.tracker.model.Visit;
import jp.chang.myclinic.tracker.model.Wqueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Track implements ModelAction {

    private static Logger logger = LoggerFactory.getLogger(Track.class);

    public static void main(String[] args){
        Service.setServerUrl("http://localhost:18080/json");
        Tracker tracker = new Tracker("http://localhost:18080/practice-log", Service.api, new Track());
        tracker.start(() -> {
        });
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

    @Override
    public void onWqueueCreated(Wqueue wqueue, Runnable toNext) {
        System.out.println("wqueue created: " + wqueue);
        toNext.run();
    }

    @Override
    public void onWqueueUpdated(Wqueue wqueue, Runnable toNext) {
        System.out.println("wqueue updated: " + wqueue);
        toNext.run();
    }

    @Override
    public void onWqueueDeleted(int visitId, Runnable toNext) {
        System.out.println("wqueue deleted: " + visitId);
        toNext.run();
    }

    @Override
    public void onTextCreated(Text text, Runnable toNext) {
        System.out.printf("text created %d: %s\n", text.getTextId(), text.getContent());
        toNext.run();
    }
}
