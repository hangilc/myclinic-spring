package jp.chang.myclinic.recordbrowser;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.utilfx.TwoColumn;

class RecordBody extends TwoColumn {

    //private static Logger logger = LoggerFactory.getLogger(RecordBody.class);

    RecordBody(VisitFull2DTO visit) {
        super(4);
        VBox left = getLeftBox();
        VBox right = getRightBox();
        visit.texts.forEach(textDTO -> left.getChildren().add(new RecordText(textDTO)));
        right.getChildren().add(new RecordHoken(visit.hoken));
        right.getChildren().add(new RecordCharge(visit.charge));
        right.getChildren().add(new Label("[処方]"));
        if( visit.drugs.size() > 0 ){
            int index = 1;
            for(DrugFullDTO drug: visit.drugs){
                right.getChildren().add(new RecordDrug(index++, drug));
            }
        }
        right.getChildren().add(new Label("[診療行為]"));
        visit.shinryouList.forEach(shinryou ->
                right.getChildren().add(new RecordShinryou(shinryou)));
        right.getChildren().add(new Label("[処置]"));
        visit.conducts.forEach(conduct ->
                right.getChildren().add(new RecordConduct(conduct)));
    }

}
