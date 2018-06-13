package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductShinryou;

class RecordConductShinryou extends TextFlow {

    private int conductShinryouId;
    private Text rep = new Text();

    RecordConductShinryou(ConductShinryou conductShinryou) {
        this.conductShinryouId = conductShinryou.getConductShinryouId();
        this.rep.textProperty().bind(conductShinryou.repProperty());
        getChildren().add(rep);
    }

    public int getConductShinryouId() {
        return conductShinryouId;
    }
}
