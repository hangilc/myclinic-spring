package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordText extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordText.class);

    RecordText(TextDTO textDTO) {
        getStyleClass().add("record-text");
        getChildren().add(new Text(textDTO.content));
    }

}
