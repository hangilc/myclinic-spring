package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConductShinryou {

    private int conductShinryouId;
    private StringProperty rep = new SimpleStringProperty();

    ConductShinryou(int conductShinryouId, String name) {
        this.conductShinryouId = conductShinryouId;
        this.rep.setValue(name);
    }

    public int getConductShinryouId() {
        return conductShinryouId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }
}
