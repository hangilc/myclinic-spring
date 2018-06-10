package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.utilfx.TwoColumn;

import java.util.ArrayList;
import java.util.List;

public class Record extends VBox {

    private int visitId;
    private TwoColumn body = new TwoColumn(4);

    public Record(Visit visit){
        this.visitId = visit.getVisitId();
        getChildren().addAll(
                new RecordTitle(visit),
                body
        );
        addHoken(visit);
    }

    public int getVisitId() {
        return visitId;
    }

    public void addText(Text text){
        RecordText recordText = new RecordText(text);
        body.getLeftBox().getChildren().add(recordText);
    }

    private void addHoken(Visit visit){
        TextFlow textFlow = new TextFlow();
        javafx.scene.text.Text text = new javafx.scene.text.Text();
        text.textProperty().bind(visit.hokenRepProperty());
        textFlow.getChildren().add(text);
        body.getRightBox().getChildren().add(textFlow);
    }
}
