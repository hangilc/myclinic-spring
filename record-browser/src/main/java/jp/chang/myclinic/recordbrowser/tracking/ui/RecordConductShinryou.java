package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductShinryouModel;

class RecordConductShinryou extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordConductShinryou.class);
    private int conductShinryouId;

    RecordConductShinryou(ConductShinryouModel model) {
        this.conductShinryouId = model.getConductShinryouId();
        getChildren().add(new TextFlow(new Text(model.getRep())));
    }

    public int getConductShinryouId() {
        return conductShinryouId;
    }
}
