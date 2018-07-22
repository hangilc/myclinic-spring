package jp.chang.myclinic.recordbrowser.tracking.ui;

import javafx.scene.text.TextFlow;
import jp.chang.myclinic.recordbrowser.tracking.model.TextModel;

class RecordText extends TextFlow {

    private int textId;

    RecordText(TextModel textModel){
        this.textId = textModel.getTextId();
        getStyleClass().add("record-text");
        javafx.scene.text.Text fxText = new javafx.scene.text.Text();
        fxText.textProperty().bind(textModel.contentProperty());
        getChildren().add(fxText);
    }

    public int getTextId() {
        return textId;
    }
}
