package jp.chang.myclinic.frontend;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FrontendBackend implements Frontend {

    private DbBackend dbBackend;

    public FrontendBackend(DbBackend dbBackend){
        this.dbBackend = dbBackend;
    }

    private <T> CompletableFuture<T> query(DbBackend.QueryStatement<T> q){
        return CompletableFuture.completedFuture(dbBackend.query(q));
    }

    private <T> CompletableFuture<T> tx(DbBackend.QueryStatement<T> q){
        return CompletableFuture.completedFuture(dbBackend.tx(q));
    }

    private CompletableFuture<Void> txProc(DbBackend.ProcStatement p){
        dbBackend.txProc(p);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return query(backend -> backend.getPatient(patientId));
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return query(backend -> backend.batchGetShinryouAttr(shinryouIds));
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return query(backend -> backend.batchGetDrugAttr(drugIds));
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return query(backend -> backend.batchGetShouki(visitIds));
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return tx(backend -> {
            backend.enterText(text);
            return text.textId;
        });
    }

    @Override
    public CompletableFuture<Void> updateText(TextDTO text) {
        return txProc(backend -> backend.updateText(text));
    }

    @Override
    public CompletableFuture<Void> deleteText(int textId) {
        return txProc(backend -> backend.deleteText(textId));
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return query(backend -> backend.getVisit(visitId));
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage) {
        return query(backend -> backend.listRecentVisitWithPatient(page, itemsPerPage));
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return query(be -> be.getHoken(visitId));
    }

}
