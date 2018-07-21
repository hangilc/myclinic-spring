package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jp.chang.myclinic.recordbrowser.tracking.model.ConductModel;

class RecordConduct extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordConduct.class);
    private int conductId;

    RecordConduct(ConductModel conductModel) {
        super(0);
        Text kindText = new Text(conductModel.getConductKind().getKanjiRep());
        getChildren().addAll(
            kindText
        );
    }

    public int getConductId() {
        return conductId;
    }
}
