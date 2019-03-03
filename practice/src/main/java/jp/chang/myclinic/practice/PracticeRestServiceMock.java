package jp.chang.myclinic.practice;

import jp.chang.myclinic.clientmock.ServiceMock;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

class PracticeRestServiceMock implements PracticeRestService {

    private ServiceMock api;

    PracticeRestServiceMock(ServiceMock api){
        this.api = api;
    }

    public CompletableFuture<Integer> enterPatient(PatientDTO patient){
        return api.enterPatient(patient);
    }

    public CompletableFuture<Integer> startVisit(int patientId, LocalDateTime at){
        return api.startVisit(patientId, DateTimeUtil.toSqlDateTime(at));
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return api.batchGetShinryouAttr(shinryouIds);
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return api.batchGetDrugAttr(drugIds);
    }

    @Override
    public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return api.batchGetShouki(visitIds);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return api.getPatient(patientId);
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return api.getVisit(visitId);
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        throw new RuntimeException("not implemented");
    }
}
