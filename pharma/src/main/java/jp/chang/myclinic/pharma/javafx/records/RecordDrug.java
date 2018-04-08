package jp.chang.myclinic.pharma.javafx.records;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordDrug extends TextFlow {

    private static Logger logger = LoggerFactory.getLogger(RecordDrug.class);

    RecordDrug(int index, DrugFullDTO drug, String highlight) {
        getChildren().addAll(
                new Text(index + ") "),
                createRep(drug, highlight)
        );
    }

    private Node createRep(DrugFullDTO drug, String highlight){
        Text text = new Text(DrugUtil.drugRep(drug));
        if( drug.master.name.equals(highlight)){
            text.getStyleClass().add("highlight");
        }
        return text;
    }

}
