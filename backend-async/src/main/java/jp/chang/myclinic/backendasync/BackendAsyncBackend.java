package jp.chang.myclinic.backendasync;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackendAsyncBackend implements BackendAsync {

    private Backend backend;

    public BackendAsyncBackend(Backend backend) {
        this.backend = backend;
    }

    private <T> CompletableFuture<T> future(T t) {
        return CompletableFuture.completedFuture(t);
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        backend.enterPatient(patient);
        return future(patient.patientId);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return future(backend.getPatient(patientId));
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        return future(backend.startVisit(patientId, at));
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return future(backend.getHoken(visitId));
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return future(backend.batchGetShinryouAttr(shinryouIds));
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return future(backend.batchGetDrugAttr(drugIds));
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return future(backend.batchGetShouki(visitIds));
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return future(backend.getVisit(visitId));
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        backend.enterText(text);
        return future(text.textId);
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        return future(backend.getText(textId));
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO text) {
        backend.updateText(text);
        return future(true);
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        backend.deleteText(textId);
        return future(true);
    }

    @Override()
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        return future(backend.listText(visitId));
    }

}
