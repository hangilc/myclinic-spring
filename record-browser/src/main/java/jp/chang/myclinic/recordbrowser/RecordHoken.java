package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.util.HokenUtil;

class RecordHoken extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordHoken.class);

    RecordHoken(HokenDTO hoken) {
        String s = HokenUtil.hokenRep(hoken);
        getChildren().add(new Text(s));
    }

}
