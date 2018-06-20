package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.util.KizaiUtil;

public class ConductKizai {

    private int conductKizaiId;
    private StringProperty rep = new SimpleStringProperty();

    public ConductKizai(ConductKizaiDTO dto, KizaiMasterDTO master) {
        this.conductKizaiId = dto.conductKizaiId;
        this.rep.setValue(KizaiUtil.kizaiRep(dto, master));
    }

    public int getConductKizaiId() {
        return conductKizaiId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }
}
