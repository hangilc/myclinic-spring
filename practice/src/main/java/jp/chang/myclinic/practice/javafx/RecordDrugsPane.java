package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugMenu;

import java.util.List;

public class RecordDrugsPane extends VBox {

    private int index = 1;

    public RecordDrugsPane(List<DrugFullDTO> drugs, int patientId, int visitId, String at){
        setAlignment(Pos.TOP_LEFT);
        DrugMenu menu = new DrugMenu(patientId, visitId, at);
        getChildren().add(menu);
        drugs.forEach(this::addDrug);
    }

    public void addDrug(DrugFullDTO drug){
        RecordDrug recordDrug = new RecordDrug(drug, index++);
        getChildren().add(recordDrug);
    }

    public void modifyDrugDays(int drugId, int days) {
        RecordDrug recordDrug = findRecordDrug(drugId);
        if( recordDrug != null ){
            recordDrug.modifyDays(days);
        }
    }

    public void deleteDrug(int drugId) {
        RecordDrug recordDrug = findRecordDrug(drugId);
        if( recordDrug != null ){
            getChildren().remove(recordDrug);
        }
    }

    private RecordDrug findRecordDrug(int drugId){
        for(Node node: getChildren()){
            if( node instanceof RecordDrug ){
                RecordDrug recordDrug = (RecordDrug)node;
                if( recordDrug.getDrugId() == drugId ){
                    return recordDrug;
                }
            }
        }
        return null;
    }
}
