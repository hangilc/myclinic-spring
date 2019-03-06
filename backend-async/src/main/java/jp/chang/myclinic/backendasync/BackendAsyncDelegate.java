package jp.chang.myclinic.backendasync;

import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackendAsyncDelegate implements BackendAsync {

    private BackendAsync delegate;

    public BackendAsyncDelegate(BackendAsync delegate) {
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO text) {
        return delegate.updateText(text);
    }

    @Override
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        return delegate.listText(visitId);
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return delegate.enterText(text);
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return delegate.getVisit(visitId);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return delegate.getPatient(patientId);
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return delegate.batchGetShinryouAttr(shinryouIds);
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        return delegate.startVisit(patientId, at);
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return delegate.batchGetShouki(visitIds);
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        return delegate.enterPatient(patient);
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return delegate.batchGetDrugAttr(drugIds);
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return delegate.getHoken(visitId);
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        return delegate.getText(textId);
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        return delegate.deleteText(textId);
    }
}
