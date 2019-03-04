package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.events.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class RecordsPane extends VBox {

    private ExecEnv execEnv;

    RecordsPane(){
        setFillWidth(true);
        addEventHandler(EventTypes.visitDeletedEventType, event -> deleteRecord(event.getVisitId()));
        addEventHandler(EventTypes.drugEnteredEventType, event -> addDrug(event.getEnteredDrug(), event.getAttr()));
        addEventHandler(EventTypes.drugDaysModifiedEventType, this::drugDaysModified);
        addEventHandler(EventTypes.drugDeletedEventType, this::drugDeleted);
        addEventHandler(ShinryouEnteredEvent.eventType, this::onShinryouEntered);
        addEventHandler(ShinryouDeletedEvent.eventType, this::onShinryouDeleted);
        addEventHandler(ConductEnteredEvent.eventType, this::onConductEntered);
        addEventHandler(ConductDeletedEvent.eventType, this::onConductDeleted);
        addEventHandler(TextEnteredEvent.eventType, this::onTextEntered);
        PracticeEnv.INSTANCE.addShoukiFormChangeListener(this::onShoukiChanged);
    }

    void setExecEnv(ExecEnv lib){
        this.execEnv = lib;
    }

    void addRecord(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                          Map<Integer, DrugAttrDTO> drugAttrMap, Map<Integer, ShoukiDTO> shoukiMap){
        Record record = new Record(visit, shinryouAttrMap, drugAttrMap, shoukiMap.get(visit.visit.visitId),
                execEnv);
        if( execEnv.mainPaneService.getCurrentVisitId() == visit.visit.visitId ){
            record.styleAsCurrentVisit();
        } else if( execEnv.mainPaneService.getTempVisitId() == visit.visit.visitId ){
            record.styleAsTempVisit();
        }
        getChildren().add(record);
    }

    private void deleteRecord(int visitId){
        findRecord(visitId).ifPresent(record -> getChildren().remove(record));
    }

    Optional<Record> findRecord(int visitId){
        for(Node node :getChildren()){
            if( node instanceof Record ){
                Record r = (Record)node;
                if( r.getVisitId() == visitId ){
                    return Optional.of(r);
                }
            }
        }
        return Optional.empty();
    }

    List<Record> listRecord() {
        return getChildren().stream().filter(n -> n instanceof Record)
                .map(n -> (Record)n).collect(Collectors.toList());
    }

    private void addDrug(DrugFullDTO drug, DrugAttrDTO attr){
        findRecord(drug.drug.visitId).ifPresent(record -> record.addDrug(drug, attr));
    }

    private void drugDaysModified(DrugDaysModifiedEvent event){
        findRecord(event.getVisitId()).ifPresent(record ->
                record.modifyDrugDays(event.getDrugId(), event.getDays()));
    }

    private void drugDeleted(DrugDeletedEvent event){
        findRecord(event.getVisitId()).ifPresent(record ->
                record.deleteDrug(event.getDrugId()));
    }

    private void onShinryouEntered(ShinryouEnteredEvent event){
        ShinryouFullDTO shinryou = event.getShinryou();
        findRecord(shinryou.shinryou.visitId).ifPresent(record ->
                record.insertShinryou(shinryou, event.getAttr()));
    }

    private void onShinryouDeleted(ShinryouDeletedEvent event) {
        int visitId = event.getVisitId();
        findRecord(visitId).ifPresent(record ->
                record.deleteShinryou(event.getShinryouId()));
    }

    private void onConductEntered(ConductEnteredEvent event){
        ConductFullDTO conduct = event.getConduct();
        findRecord(conduct.conduct.visitId).ifPresent(record ->
                record.addConduct(conduct));
    }

    private void onConductDeleted(ConductDeletedEvent event){
        findRecord(event.getVisitId()).ifPresent(record ->
                record.deleteConduct(event.getConductId()));
    }

    private void onTextEntered(TextEnteredEvent event){
        findRecord(event.getEnteredText().visitId).ifPresent(record ->
                record.appendText(event.getEnteredText()));
    }

    private void onShoukiChanged(int visitId, ShoukiDTO shoukiDTO){
        findRecord(visitId).ifPresent(record ->
                record.setShouki(shoukiDTO));
    }

}
