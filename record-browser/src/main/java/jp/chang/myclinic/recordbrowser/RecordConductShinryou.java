package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordConductShinryou extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordConductShinryou.class);

    RecordConductShinryou(ConductShinryouFullDTO shinryou) {
        getChildren().add(new Text(shinryou.master.name));
    }

}
