package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.util.KizaiUtil;

class RecordConductKizai extends TextFlow {

    //private static Logger logger = LoggerFactory.getLogger(RecordConductKizai.class);

    RecordConductKizai(ConductKizaiFullDTO kizai) {
        String s = KizaiUtil.kizaiRep(kizai);
        getChildren().add(new Text(s));
    }

}
