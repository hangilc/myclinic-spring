package jp.chang.myclinic.pharma.javafx.records;

import javafx.scene.control.Label;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitTextDrugDTO;
import jp.chang.myclinic.pharma.javafx.lib.TwoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Body extends TwoColumn {

    private static Logger logger = LoggerFactory.getLogger(Body.class);

    Body(VisitTextDrugDTO item, String highlight) {
        super(8);
        getRightBox().getStyleClass().add("record-right");
        item.texts.forEach(text -> {
            getLeftBox().getChildren().add(new RecordText(text.content));
        });
        if( item.drugs.size() > 0 ){
            getRightBox().getChildren().add(new Label("Rp)"));
            int index = 1;
            for(DrugFullDTO drug: item.drugs){
                getRightBox().getChildren().add(new RecordDrug(index++, drug, highlight));
            }
        }
    }

}
