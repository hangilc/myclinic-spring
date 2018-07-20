package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShinryouModel {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouModel.class);
    private int shinryouId;
    private StringProperty rep;

    ShinryouModel(int shinryouId, String rep) {
        this.shinryouId = shinryouId;
        this.rep = new SimpleStringProperty(rep);
    }

    public int getShinryouId() {
        return shinryouId;
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
