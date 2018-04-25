package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordDrug extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordDrug.class);

    RecordDrug(int index, DrugFullDTO drug) {
        String s = String.format("%d)%s", index, DrugUtil.drugRep(drug));
        getChildren().add(new Text(s));
    }

}
