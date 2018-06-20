package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public class ConductShinryou {

    private int conductShinryouId;
    private StringProperty rep = new SimpleStringProperty();

    public ConductShinryou(ConductShinryouDTO dto, ShinryouMasterDTO master) {
        this.conductShinryouId = dto.conductShinryouId;
        this.rep.setValue(master.name);
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
