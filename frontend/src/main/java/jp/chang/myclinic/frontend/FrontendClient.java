package jp.chang.myclinic.frontend;

import jp.chang.myclinic.client.Client;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FrontendClient /* implements Frontend */{

    private Service.ServerAPI api;

    public FrontendClient(Service.ServerAPI api){
        this.api = api;
    }
//
//    @Override
//    public CompletableFuture<PatientDTO> getPatient(int patientId) {
//        return api.getPatient(patientId);
//    }
//
//    @Override
//    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
//        return api.batchGetShinryouAttr(shinryouIds);
//    }
//
//    @Override
//    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> shinryouIds) {
//        return api.batchGetDrugAttr(shinryouIds);
//    }
//
//    @Override
//    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
//        return api.batchGetShouki(visitIds);
//    }
//
//    @Override
//    public CompletableFuture<Integer> enterText(TextDTO text) {
//        return api.enterText(text);
//    }
//
//    @Override
//    public CompletableFuture<Void> updateText(TextDTO text) {
//        return api.updateText(text).thenAccept(v -> {});
//    }
//
//    @Override
//    public CompletableFuture<Void> deleteText(int textId) {
//        return api.deleteText(textId).thenAccept(v -> {});
//    }
//
//    @Override
//    public CompletableFuture<VisitDTO> getVisit(int visitId) {
//        return api.getVisit(visitId);
//    }
//
//    @Override
//    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage) {
//        return api.listRecentVisits(page, itemsPerPage);
//    }
//
//    @Override
//    public CompletableFuture<HokenDTO> getHoken(int visitId) {
//        return api.getHoken(visitId);
//    }
}
