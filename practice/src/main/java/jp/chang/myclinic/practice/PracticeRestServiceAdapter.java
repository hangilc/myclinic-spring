package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

class PracticeRestServiceAdapter implements PracticeRestService {

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        throw new RuntimeException("not implemented");
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
