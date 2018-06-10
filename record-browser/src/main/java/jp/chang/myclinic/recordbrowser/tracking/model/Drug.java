package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

public class Drug {
    private int drugId;
    private StringProperty rep = new SimpleStringProperty();

    public Drug(DrugDTO drugDTO, IyakuhinMasterDTO master ){
        this.drugId = drugDTO.drugId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep.set(rep);
    }
}
