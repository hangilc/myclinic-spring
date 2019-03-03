package jp.chang.myclinic.practice;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backendmock.BackendMock;
import jp.chang.myclinic.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PracticeRestServiceMock implements PracticeRestService {

    private Backend api = new BackendMock();

    private <T> CompletableFuture<T> future(T t){
        return CompletableFuture.completedFuture(t);
    }

    public CompletableFuture<Integer> enterPatient(PatientDTO patient){
        return future(api.enterPatient(patient));
    }

    public CompletableFuture<Integer> enterVisit(VisitDTO visit){
        return future(api.enterVisit(visit));
    }

    public CompletableFuture<Integer> startVisit(int patientId, LocalDateTime at){
        return future(api.startVisit(patientId, at));
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return future(api.batchGetShinryouAttr(shinryouIds));
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return future(api.batchGetDrugAttr(drugIds));
    }

    @Override
    public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return future(api.batchGetShouki(visitIds));
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return future(api.getPatient(patientId));
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return future(api.getVisit(visitId));
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return future(api.getHoken(visitId));
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return future(api.enterText(text));
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        api.updateText(textDTO);
        return future(true);
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        api.deleteText(textId);
        return future(true);
    }
}
