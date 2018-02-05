package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugMenu;
import jp.chang.myclinic.practice.javafx.events.EventTypes;

import java.util.List;

public class RecordDrugsPane extends VBox {

    private int index = 1;

    public RecordDrugsPane(List<DrugFullDTO> drugs, int patientId, int visitId, String at){
        setAlignment(Pos.TOP_LEFT);
        DrugMenu menu = new DrugMenu(patientId, visitId, at);
        getChildren().add(menu);
        drugs.forEach(this::addDrug);
        addEventHandler(EventTypes.drugEnteredEventType, drugEnteredEvent -> {
            addDrug(drugEnteredEvent.getEnteredDrug());
        });
    }

    private void addDrug(DrugFullDTO drug){
        getChildren().add(new RecordDrug(drug, index++));
    }
}
