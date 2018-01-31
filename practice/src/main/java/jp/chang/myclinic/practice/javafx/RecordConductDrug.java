package jp.chang.myclinic.practice.javafx;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;

class RecordConductDrug extends TextFlow {

    RecordConductDrug(ConductDrugFullDTO drug) {
        String text = DrugUtil.conductDrugRep(drug);
        getChildren().add(new Text(text));
    }
}
