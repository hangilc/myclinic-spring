package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jp.chang.myclinic.consts.WqueueWaitState;

public class Visit {

    private int visitId;
    private Patient patient;
    private ObjectProperty<WqueueWaitState> wqueueState = new SimpleObjectProperty<>(null);

    public Visit(int visitId, Patient patient) {
        this.visitId = visitId;
        this.patient = patient;
    }

    public int getVisitId() {
        return visitId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public WqueueWaitState getWqueueState() {
        return wqueueState.get();
    }

    public ObjectProperty<WqueueWaitState> wqueueStateProperty() {
        return wqueueState;
    }

    public void setWqueueState(WqueueWaitState wqueueState) {
        this.wqueueState.set(wqueueState);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "visitId=" + visitId +
                ", patient=" + patient +
                '}';
    }
}
