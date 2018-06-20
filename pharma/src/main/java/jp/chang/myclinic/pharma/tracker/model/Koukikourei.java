package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.util.KoukikoureiUtil;

public class Koukikourei {
    private StringProperty rep = new SimpleStringProperty();

    public Koukikourei(KoukikoureiDTO dto){
        rep.setValue(KoukikoureiUtil.rep(dto));
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
