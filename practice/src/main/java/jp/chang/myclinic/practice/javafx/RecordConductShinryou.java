package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;

class RecordConductShinryou extends TextFlow {

    RecordConductShinryou(ConductShinryouFullDTO shinryou) {
        getChildren().add(new Text(shinryou.master.name));
    }

}
