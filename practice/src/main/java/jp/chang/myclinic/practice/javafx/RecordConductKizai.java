package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.util.KizaiUtil;

class RecordConductKizai extends TextFlow {

    RecordConductKizai(ConductKizaiFullDTO kizai) {
        String text = KizaiUtil.kizaiRep(kizai);
        getChildren().add(new Text(text));
    }

}
