package jp.chang.myclinic.recordbrowser;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;

class RecordConduct extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(RecordConduct.class);

    RecordConduct(ConductFullDTO conduct) {
        super(4);
        getChildren().addAll(
                new RecordConductKind(conduct.conduct.kind),
                new RecordConductLabel(conduct.gazouLabel)
        );
        
    }

}
