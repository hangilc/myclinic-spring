package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.javafx.conduct.ConductMenu;

import java.util.List;

class RecordConductsPane extends VBox {

    private String at;
    private VBox conductList = new VBox(4);

    RecordConductsPane(List<ConductFullDTO> conducts, int visitId, String at){
        super(4);
        this.at = at;
        getChildren().addAll(
                new ConductMenu(visitId, at),
                conductList
        );
        conducts.forEach(this::addConduct);
    }

    public void addConduct(ConductFullDTO conduct){
        conductList.getChildren().add(new RecordConduct(conduct, at));
    }

    public void deleteConduct(int conductId) {
        conductList.getChildren().removeIf(node -> {
            if( node instanceof RecordConduct ){
                RecordConduct recordConduct = (RecordConduct)node;
                return recordConduct.getConductId() == conductId;
            }
            return false;
        });
    }
}
