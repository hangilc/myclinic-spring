package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RecordDrug extends TextFlow {
    private Text index = new Text();
    private Text rep = new Text();

    public RecordDrug(int drugId, int index, StringProperty rep){
        this.index.setText(String.format("%d)", index));
        this.rep.textProperty().bind(rep);
        getChildren().addAll(this.index, this.rep);
    }
}
