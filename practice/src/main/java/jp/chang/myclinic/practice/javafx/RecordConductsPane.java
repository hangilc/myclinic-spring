package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.javafx.conduct.ConductMenu;

import java.time.LocalDate;
import java.util.List;

class RecordConductsPane extends VBox {

    private LocalDate at;
    private VBox conductList = new VBox(4);

    RecordConductsPane(List<ConductFullDTO> conducts, int visitId, LocalDate at){
        super(4);
        this.at = at;
        getChildren().addAll(
                createMenu(visitId, at),
                conductList
        );
        conducts.forEach(this::addConduct);
    }

    private ConductMenu createMenu(int visitId, LocalDate at){
        ConductMenu menu = new ConductMenu(visitId, at);
        menu.setOnEnteredHandler(this::addConduct);
        return menu;
    }

    void addConduct(ConductFullDTO conduct){
        final int conductId = conduct.conduct.conductId;
        RecordConduct recordConduct = new RecordConduct(conduct, at);
        recordConduct.setOnDeletedHandler(() -> deleteConduct(conductId));
        conductList.getChildren().add(recordConduct);
    }

    void deleteConduct(int conductId) {
        conductList.getChildren().removeIf(node -> {
            if( node instanceof RecordConduct ){
                RecordConduct recordConduct = (RecordConduct)node;
                return recordConduct.getConductId() == conductId;
            }
            return false;
        });
    }
}
