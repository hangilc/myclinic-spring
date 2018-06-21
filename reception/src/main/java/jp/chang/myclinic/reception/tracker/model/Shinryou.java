package jp.chang.myclinic.reception.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;

public class Shinryou {
    private int shinryouId;
    private int visitId;
    private int shinryoucode;
    private StringProperty rep = new SimpleStringProperty();

    public Shinryou(ShinryouDTO shinryouDTO, ShinryouMasterDTO master){
        this.shinryouId = shinryouDTO.shinryouId;
        this.visitId = shinryouDTO.visitId;
        this.shinryoucode = shinryouDTO.shinryoucode;
        this.rep.setValue(master.name);
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getVisitId() {
        return visitId;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }
}
