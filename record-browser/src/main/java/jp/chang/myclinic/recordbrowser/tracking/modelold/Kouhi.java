package jp.chang.myclinic.recordbrowser.tracking.modelold;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.util.KouhiUtil;

public class Kouhi {
    private StringProperty rep = new SimpleStringProperty();

    public Kouhi(KouhiDTO dto){
        rep.setValue(KouhiUtil.rep(dto));
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
