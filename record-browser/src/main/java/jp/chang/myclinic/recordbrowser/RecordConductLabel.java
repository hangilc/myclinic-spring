package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.GazouLabelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordConductLabel extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordConductLabel.class);

    RecordConductLabel(GazouLabelDTO gazouLabel) {
        if( gazouLabel != null ){
            getChildren().add(new Text(gazouLabel.label));
        }
    }

}
