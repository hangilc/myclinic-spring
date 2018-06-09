package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.TextDTO;

public class Text {

    private int textId;
    private int visitId;
    private StringProperty content = new SimpleStringProperty();

    public Text(TextDTO dto){
        this.textId = dto.textId;
        this.visitId = dto.visitId;
                this.content.setValue(dto.content);
    }

    public int getTextId() {
        return textId;
    }

    public int getVisitId() {
        return visitId;
    }

    public String getContent() {
        return content.get();
    }

    public StringProperty contentProperty() {
        return content;
    }

    public void setContent(String content) {
        this.content.set(content);
    }
}
