package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DrugModel {

    //private static Logger logger = LoggerFactory.getLogger(DrugModel.class);
    private int drugId;
    private StringProperty rep;

    DrugModel(int drugId, String rep) {
        this.drugId = drugId;
        this.rep = new SimpleStringProperty(rep);
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

    public void setRep(String rep) {
        this.rep.set(rep);
    }
}
