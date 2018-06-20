package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jp.chang.myclinic.dto.PharmaQueueDTO;

public class PharmaQueue {

    private IntegerProperty pharmaState;
    private Visit visit;

    public PharmaQueue(PharmaQueueDTO dto, Visit visit) {
        this.pharmaState = new SimpleIntegerProperty(dto.pharmaState);
        this.visit = visit;
    }

    public int getPharmaState() {
        return pharmaState.get();
    }

    public IntegerProperty pharmaStateProperty() {
        return pharmaState;
    }

    public void setPharmaState(int pharmaState) {
        this.pharmaState.set(pharmaState);
    }

    public Visit getVisit() {
        return visit;
    }
}
