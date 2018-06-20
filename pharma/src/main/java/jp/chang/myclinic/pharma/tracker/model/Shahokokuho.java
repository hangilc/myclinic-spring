package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.ShahokokuhoUtil;

public class Shahokokuho {

    private StringProperty rep = new SimpleStringProperty();

    public Shahokokuho(ShahokokuhoDTO dto){
        rep.setValue(ShahokokuhoUtil.rep(dto));
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
