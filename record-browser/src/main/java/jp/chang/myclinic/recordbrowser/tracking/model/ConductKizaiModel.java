package jp.chang.myclinic.recordbrowser.tracking.model;

public class ConductKizaiModel {

    //private static Logger logger = LoggerFactory.getLogger(ConductKizaiModel.class);
    private int conductKizaiId;
    private String rep;

    ConductKizaiModel(int conductKizaiId, String rep) {
        this.conductKizaiId = conductKizaiId;
        this.rep = rep;
    }

    public int getConductKizaiId() {
        return conductKizaiId;
    }

    public String getRep() {
        return rep;
    }
}
