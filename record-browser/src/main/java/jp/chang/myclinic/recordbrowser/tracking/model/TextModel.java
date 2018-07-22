package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.TextDTO;

public class TextModel {

    //private static Logger logger = LoggerFactory.getLogger(TextModel.class);

    private int textId;
    private StringProperty content;

    TextModel(TextDTO textDTO) {
        this.textId = textDTO.textId;
        this.content = new SimpleStringProperty(textDTO.content);
    }

    public int getTextId() {
        return textId;
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
