package jp.chang.myclinic.pharma.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.WqueueWaitState;

class ModelImpl implements PatientList.Model {

    private int visitId;
    private StringProperty name;
    private ObjectProperty<WqueueWaitState> waitState;

    ModelImpl(int visitId, StringProperty name, ObjectProperty<WqueueWaitState> waitState) {
        this.visitId = visitId;
        this.name = name;
        this.waitState = waitState;
    }

    @Override
    public int getVisitId() {
        return visitId;
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public ObjectProperty<WqueueWaitState> waitStateProperty() {
        return waitState;
    }
}
