package jp.chang.myclinic.backendasync;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BackendAsync {

    CompletableFuture<Integer> enterPatient(PatientDTO patient);

    CompletableFuture<PatientDTO> getPatient(int patientId);

    CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at);

    CompletableFuture<HokenDTO> getHoken(int visitId);

    CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);

    CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds);

    CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);

    CompletableFuture<VisitDTO> getVisit(int visitId);

    CompletableFuture<Integer> enterText(TextDTO text);

    CompletableFuture<TextDTO> getText(int textId);

    CompletableFuture<Boolean> updateText(TextDTO text);

    CompletableFuture<Boolean> deleteText(int textId);

    CompletableFuture<List<TextDTO>> listText(int visitId);
}
