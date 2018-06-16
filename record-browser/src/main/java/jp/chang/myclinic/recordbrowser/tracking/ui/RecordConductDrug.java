package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductDrug;

class RecordConductDrug extends TextFlow {

    private int conductDrugId;
    private Text rep = new Text();

    RecordConductDrug(ConductDrug conductDrug) {
        this.conductDrugId = conductDrug.getConductDrugId();
        this.rep.textProperty().bind(conductDrug.repProperty());
        getChildren().add(rep);
    }

    public int getConductDrugId() {
        return conductDrugId;
    }
}
