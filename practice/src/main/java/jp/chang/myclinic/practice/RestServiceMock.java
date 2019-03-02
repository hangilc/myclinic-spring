package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class RestServiceMock implements PracticeRestService {

    private int serialPatientId = 1;
    private int serialVisitId = 1;
    private int serialTextId = 1;

    private Map<Integer, PatientDTO> patientRegistry = new HashMap<>();
    private Map<Integer, VisitDTO> visitRegistry = new HashMap<>();
    private Map<Integer, ShahokokuhoDTO> shahokokuhoRegistry = new HashMap<>();
    private Map<Integer, KoukikoureiDTO> koukikoureiRegistry = new HashMap<>();
    private Map<Integer, KouhiDTO> kouhiRegistry = new HashMap<>();
    private Map<Integer, TextDTO> textRegistry = new HashMap<>();
    private Map<Integer, DrugDTO> drugRegistry = new HashMap<>();

    private <T> CompletableFuture<T> future(T t){
        return CompletableFuture.completedFuture(t);
    }

    //@Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient){
        patient.patientId = serialPatientId++;
        patientRegistry.put(patient.patientId, patient);
        return future(patient.patientId);
    }

    //@Override
    public CompletableFuture<Integer> enterVisit(VisitDTO visit){
        visit.visitId = serialVisitId++;
        visitRegistry.put(visit.visitId, visit);
        return future(visit.visitId);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return future(patientRegistry.get(patientId));
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return future(visitRegistry.get(visitId));
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        VisitDTO visit = visitRegistry.get(visitId);
        HokenDTO hoken = new HokenDTO();
        if( visit.shahokokuhoId > 0 ){
            hoken.shahokokuho = shahokokuhoRegistry.get(visit.shahokokuhoId);
        }
        if( visit.koukikoureiId > 0 ){
            hoken.koukikourei = koukikoureiRegistry.get(visit.koukikoureiId);
        }
        if( visit.kouhi1Id > 0 ){
            hoken.kouhi1 = kouhiRegistry.get(visit.kouhi1Id);
        }
        if( visit.kouhi2Id > 0 ){
            hoken.kouhi2 = kouhiRegistry.get(visit.kouhi2Id);
        }
        if( visit.kouhi3Id > 0 ){
            hoken.kouhi3 = kouhiRegistry.get(visit.kouhi3Id);
        }
        return future(hoken);
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        text.textId = serialTextId++;
        textRegistry.put(text.textId, text);
        return future(text.textId);
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        textRegistry.put(textDTO.textId, textDTO);
        return future(true);
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        textRegistry.remove(textId);
        return future(true);
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return future(Collections.emptyList());
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return future(Collections.emptyList());
    }

    @Override
    public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return future(Collections.emptyList());
    }

}
