package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouFullDTO;

class RecordShinryou extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordShinryou.class);

    RecordShinryou(ShinryouFullDTO shinryou) {
        getChildren().add(new Text(shinryou.master.name));
    }

}
