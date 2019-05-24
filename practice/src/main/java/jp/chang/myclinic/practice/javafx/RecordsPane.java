package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.IntegrationService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecordsPane extends VBox {

    private CurrentPatientService currentPatientService = Context.currentPatientService;

    public RecordsPane(){
        getStyleClass().add("records-pane");
        setFillWidth(true);
        IntegrationService integ = Context.integrationService;
        integ.setOnNewText(this::onNewText);
        integ.setOnNewDrug(this::onNewDrug);
        integ.setOnNewShinryou(this::onNewShinryou);
        integ.setOnNewConduct(this::onNewConduct);
        Context.currentPatientService.setOnTempVisitIdChangedHandler(change -> {
            if( change.prevTempVisitId != 0 ) {
                findRecord(change.prevTempVisitId).ifPresent(Record::styleAsRegular);
            }
            if( change.newTempVisitId != 0 ){
                findRecord(change.newTempVisitId).ifPresent(Record::styleAsTempVisit);
            }
        });
        Context.integrationService.setOnShoukiHandler(this::onShoukiChanged);
    }

    public void addRecord(VisitFull2DTO visit, Map<Integer, ShinryouAttrDTO> shinryouAttrMap,
                          Map<Integer, DrugAttrDTO> drugAttrMap, Map<Integer, ShoukiDTO> shoukiMap){
        Record record = new Record(visit, shinryouAttrMap, drugAttrMap, shoukiMap.get(visit.visit.visitId));
        record.setOnDeletedHandler(() -> onVisitDeleted(visit.visit.visitId));
        if( currentPatientService.getCurrentVisitId() == visit.visit.visitId ){
            record.styleAsCurrentVisit();
        } else if( currentPatientService.getTempVisitId() == visit.visit.visitId ){
            record.styleAsTempVisit();
        }
        getChildren().add(record);
        layout();
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

    public List<Record> listRecord() {
        return getChildren().stream().filter(n -> n instanceof Record)
                .map(n -> (Record)n).collect(Collectors.toList());
    }

    private void onVisitDeleted(int visitId){
        findRecord(visitId)
                .ifPresent(record -> getChildren().remove(record));
    }

    private void onNewText(TextDTO text){
        findRecord(text.visitId)
                .ifPresent(record -> record.addText(text));

    }

    private void onNewDrug(DrugFullDTO drug, DrugAttrDTO attr){
        findRecord(drug.drug.visitId).ifPresent(record -> record.addDrug(drug, attr));
    }

    private void onNewShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        findRecord(shinryou.shinryou.visitId).ifPresent(record ->
                record.addShinryou(shinryou, attr));
    }

    private void onNewConduct(ConductFullDTO conduct){
        findRecord(conduct.conduct.visitId).ifPresent(record -> record.addConduct(conduct));
    }

    private void onShoukiChanged(int visitId, ShoukiDTO shoukiDTO){
        findRecord(visitId).ifPresent(record ->
                record.setShouki(shoukiDTO));
    }

}
