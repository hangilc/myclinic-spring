package jp.chang.myclinic.recordbrowser.tracking.uiold;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.modelold.ConductKizai;

class RecordConductKizai extends TextFlow {

    private int conductKizaiId;
    private Text rep = new Text();

    RecordConductKizai(ConductKizai conductKizai) {
        this.conductKizaiId = conductKizai.getConductKizaiId();
        this.rep.textProperty().bind(conductKizai.repProperty());
        getChildren().add(rep);
    }

    public int getConductKizaiId() {
        return conductKizaiId;
    }
}
