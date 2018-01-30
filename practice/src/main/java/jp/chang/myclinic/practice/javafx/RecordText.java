package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.TextDTO;

public class RecordText extends TextFlow {

    public RecordText(TextDTO text){
        getChildren().add(new Text(text.content));
    }
}
