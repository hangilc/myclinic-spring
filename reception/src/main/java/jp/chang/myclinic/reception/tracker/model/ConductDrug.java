package jp.chang.myclinic.reception.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.util.DrugUtil;

public class ConductDrug {

    private int conductDrugId;
    private StringProperty rep = new SimpleStringProperty();

    public ConductDrug(ConductDrugDTO dto, IyakuhinMasterDTO master) {
        this.conductDrugId = dto.conductDrugId;
        this.rep.setValue(DrugUtil.conductDrugRep(dto, master));
    }

    public int getConductDrugId() {
        return conductDrugId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }
}
