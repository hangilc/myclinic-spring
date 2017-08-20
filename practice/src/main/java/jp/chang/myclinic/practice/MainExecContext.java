package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.concurrent.CompletableFuture;

public interface MainExecContext {

    CompletableFuture<Void> startExam(VisitDTO visit, PatientDTO patient);

}
