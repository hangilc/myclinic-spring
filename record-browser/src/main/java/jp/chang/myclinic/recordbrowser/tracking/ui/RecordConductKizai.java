package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductKizaiModel;

class RecordConductKizai extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordConductKizai.class);
    private int conductKizaiId;

    RecordConductKizai(ConductKizaiModel model) {
        this.conductKizaiId = model.getConductKizaiId();
        getChildren().add(new Text(model.getRep()));
    }

    public int getConductKizaiId() {
        return conductKizaiId;
    }
}
