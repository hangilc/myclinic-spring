package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductDrugModel;

class RecordConductDrug extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordConductDrug.class);
    private int conductDrugId;

    RecordConductDrug(ConductDrugModel model) {
        this.conductDrugId = model.getConductDrugId();
        getChildren().add(new TextFlow(new Text(model.getRep())));
    }

    public int getConductDrugId() {
        return conductDrugId;
    }
}
