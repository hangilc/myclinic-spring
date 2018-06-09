package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.utilfx.TwoColumn;

public class Record extends VBox {

    private int visitId;
    private TwoColumn body = new TwoColumn(4);

    public Record(Visit visit){
        this.visitId = visit.getVisitId();
        getChildren().addAll(
                new RecordTitle(visit),
                body
        );
    }

    public int getVisitId() {
        return visitId;
    }

    public void addText(Text text){
        RecordText recordText = new RecordText(text);
        body.getLeftBox().getChildren().add(recordText);
    }
}
