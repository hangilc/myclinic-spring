package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;

class RecordConduct extends StackPane {

    RecordConduct(ConductFullDTO conduct){
        getChildren().add(createDisp(conduct));
    }

    private Node createDisp(ConductFullDTO conduct){
        VBox disp = new VBox();
        String kindText = ConductKind.codeToKanjiRep(conduct.conduct.kind);
        disp.getChildren().add(new Label("<" + kindText + ">"));
        GazouLabelDTO gazouLabel = conduct.gazouLabel;
        if( gazouLabel != null && gazouLabel.label != null && !gazouLabel.label.isEmpty() ){
            disp.getChildren().add(new Label(gazouLabel.label));
        }
        if( conduct.conductShinryouList != null ){
            conduct.conductShinryouList.forEach(shinryou -> {
                disp.getChildren().add(new RecordConductShinryou(shinryou));
            });
        }
        if( conduct.conductDrugs != null ){
            conduct.conductDrugs.forEach(drug -> {
                disp.getChildren().add(new RecordConductDrug(drug));
            });
        }
        if( conduct.conductKizaiList != null ){
            conduct.conductKizaiList.forEach(kizai -> {
                disp.getChildren().add(new RecordConductKizai(kizai));
            });
        }
        return disp;
    }

}
