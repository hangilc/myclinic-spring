package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.util.DrugUtil;

public class Drug {
    private int drugId;
    private int visitId;
    private StringProperty rep = new SimpleStringProperty();

    public Drug(DrugDTO drugDTO, IyakuhinMasterDTO master ){
        this.drugId = drugDTO.drugId;
        this.visitId = drugDTO.visitId;
        this.rep.setValue(DrugUtil.drugRep(drugDTO, master));
    }

    public int getDrugId() {
        return drugId;
    }

    public int getVisitId() {
        return visitId;
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
