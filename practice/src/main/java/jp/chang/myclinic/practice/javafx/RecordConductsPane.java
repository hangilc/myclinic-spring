package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;

import java.util.List;

class RecordConductsPane extends VBox {

    RecordConductsPane(List<ConductFullDTO> conducts){
        conducts.forEach(this::addConduct);
    }

    private void addConduct(ConductFullDTO conduct){
        getChildren().add(new RecordConduct(conduct));
    }
}
