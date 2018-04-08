package jp.chang.myclinic.pharma.javafx.records;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordText extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordText.class);

    RecordText(String content) {
        getChildren().add(new Text(content));
    }

}
