package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface RestService {
    CompletableFuture<Integer> enterPatient(PatientDTO patient);

    CompletableFuture<Integer> enterVisit(VisitDTO visit);

    CompletableFuture<PatientDTO> getPatient(int patientId);

    CompletableFuture<VisitDTO> getVisit(int visitId);

    CompletableFuture<HokenDTO> getHoken(int visitId);

    CompletableFuture<Integer> enterText(TextDTO text);

    CompletableFuture<Boolean> updateText(TextDTO textDTO);

    CompletableFuture<Boolean> deleteText(int textId);

    CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);

    CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds);

    CompletionStage<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);

}
