package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.practice.javafx.events.*;

public class RecordsPane extends VBox {

    RecordsPane(){
        setFillWidth(true);
        addEventHandler(EventTypes.visitDeletedEventType, event -> {
            deleteRecord(event.getVisitId());
        });
        addEventHandler(EventTypes.drugEnteredEventType, event -> {
            addDrug(event.getEnteredDrug());
        });
        addEventHandler(EventTypes.drugDaysModifiedEventType, this::drugDaysModified);
        addEventHandler(EventTypes.drugDeletedEventType, this::drugDeleted);
        addEventHandler(ShinryouEnteredEvent.eventType, this::onShinryouEntered);
        addEventHandler(ShinryouDeletedEvent.eventType, this::onShinryouDeleted);
        addEventHandler(ConductEnteredEvent.eventType, this::onConductEntered);
        addEventHandler(ConductDeletedEvent.eventType, this::onConductDeleted);
    }

    public void addRecord(VisitFull2DTO visit){
        Record record = new Record(visit);
        getChildren().add(record);
    }

    private void deleteRecord(int visitId){
        Record record = findRecord(visitId);
        if( record != null ){
            getChildren().remove(record);
        }
    }

    private Record findRecord(int visitId){
        for(Node node :getChildren()){
            if( node instanceof Record ){
                Record r = (Record)node;
                if( r.getVisitId() == visitId ){
                    return r;
                }
            }
        }
        return null;
    }

    private void addDrug(DrugFullDTO drug){
        Record record = findRecord(drug.drug.visitId);
        if( record != null ){
            record.addDrug(drug);
        }
    }

    private void drugDaysModified(DrugDaysModifiedEvent event){
        Record record = findRecord(event.getVisitId());
        if( record != null ){
            record.modifyDrugDays(event.getDrugId(), event.getDays());
        }
    }

    private void drugDeleted(DrugDeletedEvent event){
        Record record = findRecord(event.getVisitId());
        if( record != null ){
            record.deleteDrug(event.getDrugId());
        }
    }

    private void onShinryouEntered(ShinryouEnteredEvent event){
        ShinryouFullDTO shinryou = event.getShinryou();
        Record record = findRecord(shinryou.shinryou.visitId);
        if( record != null ){
            record.insertShinryou(shinryou);
        }
    }

    private void onShinryouDeleted(ShinryouDeletedEvent event) {
        int visitId = event.getVisitId();
        Record record = findRecord(visitId);
        if( record != null ){
            record.deleteShinryou(event.getShinryouId());
        }
    }

    private void onConductEntered(ConductEnteredEvent event){
        ConductFullDTO conduct = event.getConduct();
        Record record = findRecord(conduct.conduct.visitId);
        if( record != null ){
            record.addConduct(conduct);
        }
    }

    private void onConductDeleted(ConductDeletedEvent event){
        Record record = findRecord(event.getVisitId());
        if( record != null ){
            record.deleteConduct(event.getConductId());
        }
    }

}
