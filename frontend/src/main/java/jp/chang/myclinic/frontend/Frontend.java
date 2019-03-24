package jp.chang.myclinic.frontend;

import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Frontend {

    CompletableFuture<PatientDTO> getPatient(int patientId);
    CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);
    CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> shinryouIds);
    CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);
    CompletableFuture<Integer> enterText(TextDTO text);
    CompletableFuture<Void> updateText(TextDTO text);
    CompletableFuture<Void> deleteText(int textId);
    CompletableFuture<VisitDTO> getVisit(int visitId);
    CompletableFuture<HokenDTO> getHoken(int visitId);
}
