package jp.chang.myclinic.backendasync;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackendAsyncClient implements BackendAsync {

    private Service.ServerAPI api;

    public BackendAsyncClient(Service.ServerAPI api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        return api.enterPatient(patient);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return api.getPatient(patientId);
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        String atStr = DateTimeUtil.toSqlDateTime(at);
        return api.startVisit(patientId, atStr).thenCompose(api::getVisit);
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return api.getHoken(visitId);
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
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return api.batchGetShouki(visitIds);
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return api.getVisit(visitId);
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return api.enterText(text);
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        return api.getText(textId);
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO text) {
        return api.updateText(text);
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        return api.deleteText(textId);
    }

    @Override()
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        return api.listText(visitId);
    }
}
