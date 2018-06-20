package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.util.DrugUtil;

public class Drug {
    private int drugId;
    private int visitId;
    private StringProperty rep = new SimpleStringProperty();

    public Drug(DrugDTO drugDTO, IyakuhinMasterDTO master){
        this.drugId = drugDTO.drugId;
        this.visitId = drugDTO.visitId;
        updateRep(drugDTO, master);
    }

    public int getDrugId() {
        return drugId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }

    final public void updateRep(DrugDTO drugDTO, IyakuhinMasterDTO master){
        this.rep.setValue(DrugUtil.drugRep(drugDTO, master));
    }

}
