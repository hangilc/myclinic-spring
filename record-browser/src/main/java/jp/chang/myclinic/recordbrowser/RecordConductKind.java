package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.ConductKind;

class RecordConductKind extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordConductKind.class);

    RecordConductKind(int kind) {

        String s = "<" + ConductKind.codeToKanjiRep(kind) + ">";
        getChildren().add(new Text(s));
    }

}
