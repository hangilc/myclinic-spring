package jp.chang.myclinic.reception.tracker.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jp.chang.myclinic.dto.WqueueDTO;

public class Wqueue {

    //private static Logger logger = LoggerFactory.getLogger(Wqueue.class);
    private IntegerProperty waitState;
    private Visit visit;

    public Wqueue(WqueueDTO wqueueDTO, Visit visit) {
        this.waitState = new SimpleIntegerProperty(wqueueDTO.waitState);
        this.visit = visit;
    }

    public int getWaitState() {
        return waitState.get();
    }

    public IntegerProperty waitStateProperty() {
        return waitState;
    }

    public void setWaitState(int waitState) {
        this.waitState.set(waitState);
    }

    public Visit getVisit() {
        return visit;
    }

    @Override
    public String toString() {
        return "Wqueue{" +
                "waitState=" + waitState +
                ", visit=" + visit +
                '}';
    }
}
