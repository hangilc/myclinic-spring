package jp.chang.myclinic.recordbrowser.tracking.model;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductDTO;

public class ConductModel {

    //private static Logger logger = LoggerFactory.getLogger(ConductModel.class);
    private int conductId;
    private ConductKind conductKind;

    ConductModel(ConductDTO conductDTO) {
        this.conductId = conductDTO.conductId;
        this.conductKind = ConductKind.fromCode(conductDTO.kind);
    }

    public int getConductId() {
        return conductId;
    }

    public ConductKind getConductKind() {
        return conductKind;
    }

}
