package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;

class RecordText extends TextFlow {

    private int textId;

    RecordText(Text text){
        this.textId = text.getTextId();
        getStyleClass().add("record-text");
        javafx.scene.text.Text fxText = new javafx.scene.text.Text();
        fxText.textProperty().bind(text.contentProperty());
        getChildren().add(fxText);
    }

    public int getTextId() {
        return textId;
    }
}
