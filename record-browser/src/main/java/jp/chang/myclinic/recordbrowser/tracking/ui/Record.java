package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.RecordModel;
import jp.chang.myclinic.utilfx.TwoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Record extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Record.class);

    private int visitId;
    private RecordTitle title;
    private TwoColumn body = new TwoColumn(4);

    Record(RecordModel recordModel) {
        this.visitId = recordModel.getVisitId();
        this.title = new RecordTitle(recordModel);
        getChildren().add(title);
    }

    public int getVisitId() {
        return visitId;
    }
}
