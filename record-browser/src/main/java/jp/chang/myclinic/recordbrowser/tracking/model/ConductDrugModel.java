package jp.chang.myclinic.recordbrowser.tracking.model;

public class ConductDrugModel {

    //private static Logger logger = LoggerFactory.getLogger(ConductDrugModel.class);
    private int conductDrugId;
    private String rep;

    ConductDrugModel(int conductDrugId, String rep) {
        this.conductDrugId = conductDrugId;
        this.rep = rep;
    }

    public int getConductDrugId() {
        return conductDrugId;
    }

    public String getRep() {
        return rep;
    }
}
