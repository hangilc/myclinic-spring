package jp.chang.myclinic.practice.javafx;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;

class RecordConduct extends VBox {

    RecordConduct(ConductFullDTO conduct){
        String kindText = ConductKind.codeToKanjiRep(conduct.conduct.kind);
        getChildren().add(new Label("<" + kindText + ">"));
        GazouLabelDTO gazouLabel = conduct.gazouLabel;
        if( gazouLabel != null && gazouLabel.label != null && !gazouLabel.label.isEmpty() ){
            getChildren().add(new Label(gazouLabel.label));
        }
        if( conduct.conductShinryouList != null ){
            conduct.conductShinryouList.forEach(this::addShinryou);
        }
        if( conduct.conductDrugs != null ){
            conduct.conductDrugs.forEach(this::addDrug);
        }
        if( conduct.conductKizaiList != null ){
            conduct.conductKizaiList.forEach(this::addKizai);
        }
    }

    private void addShinryou(ConductShinryouFullDTO shinryou){
        getChildren().add(new RecordConductShinryou(shinryou));
    }

    private void addDrug(ConductDrugFullDTO drug){
        getChildren().add(new RecordConductDrug(drug));
    }

    private void addKizai(ConductKizaiFullDTO kizai){
        getChildren().add(new RecordConductKizai(kizai));
    }

}
