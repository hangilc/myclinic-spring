package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.drug.DrugMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class RecordDrugsPane extends VBox {

    private int index = 1;
    private VisitDTO visit;
    private DrugMenu menu;

    RecordDrugsPane(List<DrugFullDTO> drugs, VisitDTO visit, Map<Integer, DrugAttrDTO> drugAttrMap) {
        this.visit = visit;
        setAlignment(Pos.TOP_LEFT);
        this.menu = new DrugMenu(visit);
        menu.setOnDrugEnteredHandler(this::addDrug);
        menu.setOnDrugDeletedHandler(this::onDrugDeleted);
        getChildren().add(menu);
        drugs.forEach(drug -> addDrug(drug, drugAttrMap.get(drug.drug.drugId)));
    }

    void simulateNewDrugButtonClick() {
        menu.simulateNewDrugButtonClick();
    }

    Optional<DrugEnterForm> findDrugEnterForm() {
        return menu.findDrugEnterForm();
    }

    List<RecordDrug> listDrug() {
        List<RecordDrug> drugs = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof RecordDrug) {
                RecordDrug recordDrug = (RecordDrug) node;
                drugs.add(recordDrug);
            }
        }
        return drugs;
    }

    void addDrug(DrugFullDTO drug, DrugAttrDTO attr) {
        if (drug.drug.visitId != visit.visitId) {
            throw new RuntimeException("Inconsisitent visitId in drug.");
        }
        RecordDrug recordDrug = new RecordDrug(drug, visit, index++, attr);
        recordDrug.setOnDeletedHandler(() -> onDrugDeleted(List.of(drug.drug.drugId)));
        getChildren().add(recordDrug);
    }

    void onDrugDeleted(List<Integer> drugIds){
        getChildren().removeIf(node -> {
            if( node instanceof RecordDrug ){
                RecordDrug recordDrug = (RecordDrug)node;
                return drugIds.contains(recordDrug.getDrugId());
            } else {
                return false;
            }
        });
        reIndex();
    }

    void modifyDrugDays(int drugId, int days) {
        RecordDrug recordDrug = findRecordDrug(drugId);
        if (recordDrug != null) {
            recordDrug.modifyDays(days);
        }
    }

    void deleteDrug(int drugId) {
        onDrugDeleted(List.of(drugId));
    }

    private RecordDrug findRecordDrug(int drugId) {
        for (Node node : getChildren()) {
            if (node instanceof RecordDrug) {
                RecordDrug recordDrug = (RecordDrug) node;
                if (recordDrug.getDrugId() == drugId) {
                    return recordDrug;
                }
            }
        }
        return null;
    }

    private void reIndex() {
        this.index = 1;
        for (Node node : getChildren()) {
            if (node instanceof RecordDrug) {
                RecordDrug recordDrug = (RecordDrug) node;
                recordDrug.setIndex(this.index++);
            }
        }
    }

}
