package jp.chang.myclinic.recordbrowser.tracking.model;

public class ConductShinryouModel {

    //private static Logger logger = LoggerFactory.getLogger(ConductShinryouModel.class);
    private int conductShinryouId;
    private String rep;

    ConductShinryouModel(int conductShinryouId, String rep) {
        this.conductShinryouId = conductShinryouId;
        this.rep = rep;
    }

    public int getConductShinryouId() {
        return conductShinryouId;
    }

    public String getRep() {
        return rep;
    }
}
