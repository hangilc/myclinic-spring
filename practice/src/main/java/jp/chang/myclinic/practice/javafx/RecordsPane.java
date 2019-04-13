package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.IntegrationService;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.events.ConductDeletedEvent;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouDeletedEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecordsPane extends VBox {

    private CurrentPatientService currentPatientService = Context.currentPatientService;

    public RecordsPane(){
        setFillWidth(true);
        IntegrationService integ = Context.integrationService;
        integ.setOnNewText(this::onNewText);
        integ.setOnNewDrug(this::onNewDrug);
        addEventHandler(ShinryouEnteredEvent.eventType, this::onShinryouEntered);
        addEventHandler(ShinryouDeletedEvent.eventType, this::onShinryouDeleted);
        addEventHandler(ConductEnteredEvent.eventType, this::onConductEntered);
        addEventHandler(ConductDeletedEvent.eventType, this::onConductDeleted);
        PracticeEnv.INSTANCE.addShoukiFormChangeListener(this::onShoukiChanged);
    }

    public void addRecord(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                          Map<Integer, DrugAttrDTO> drugAttrMap, Map<Integer, ShoukiDTO> shoukiMap){
        Record record = new Record(visit, shinryouAttrMap, drugAttrMap, shoukiMap.get(visit.visit.visitId));
        if( currentPatientService.getCurrentVisitId() == visit.visit.visitId ){
            record.styleAsCurrentVisit();
        } else if( currentPatientService.getTempVisitId() == visit.visit.visitId ){
            record.styleAsTempVisit();
        }
        getChildren().add(record);
    }

    public Optional<Record> findRecord(int visitId){
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

    private void onNewText(TextDTO text){
        findRecord(text.visitId)
                .ifPresent(record -> record.addText(text));

    }

    private void onNewDrug(DrugFullDTO drug, DrugAttrDTO attr){
        findRecord(drug.drug.visitId).ifPresent(record -> record.addDrug(drug, attr));
    }

    private void onShinryouEntered(ShinryouEnteredEvent event){
        ShinryouFullDTO shinryou = event.getShinryou();
        findRecord(shinryou.shinryou.visitId).ifPresent(record ->
                record.addShinryou(shinryou, event.getAttr()));
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

    private void onShoukiChanged(int visitId, ShoukiDTO shoukiDTO){
        findRecord(visitId).ifPresent(record ->
                record.setShouki(shoukiDTO));
    }

}
