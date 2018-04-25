package jp.chang.myclinic.recordbrowser;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordConductDrug extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordConductDrug.class);

    RecordConductDrug(ConductDrugFullDTO drug) {
        String s = DrugUtil.conductDrugRep(drug);
        getChildren().add(new Text(s));
    }

}
