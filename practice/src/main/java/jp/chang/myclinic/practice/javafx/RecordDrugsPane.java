package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;

import java.util.List;

public class RecordDrugsPane extends VBox {

    private int index = 1;

    public RecordDrugsPane(List<DrugFullDTO> drugs){
        drugs.forEach(this::addDrug);
    }

    private void addDrug(DrugFullDTO drug){
        getChildren().add(new RecordDrug(drug, index++));
    }
}
