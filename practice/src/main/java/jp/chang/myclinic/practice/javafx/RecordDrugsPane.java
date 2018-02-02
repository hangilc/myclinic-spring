package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugMenu;

import java.util.List;

public class RecordDrugsPane extends VBox {

    private int index = 1;

    public RecordDrugsPane(List<DrugFullDTO> drugs){
        setAlignment(Pos.TOP_LEFT);
        DrugMenu menu = new DrugMenu();
        getChildren().add(menu);
        drugs.forEach(this::addDrug);
    }

    private void addDrug(DrugFullDTO drug){
        getChildren().add(new RecordDrug(drug, index++));
    }
}
