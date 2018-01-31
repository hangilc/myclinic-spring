package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouFullDTO;

class RecordShinryou extends TextFlow {

    RecordShinryou(ShinryouFullDTO shinryou){
        String text = shinryou.master.name;
        getChildren().add(new Text(text));
    }
}
