package jp.chang.myclinic.practice.javafx.shohousen;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public interface ShohousenRestRequirement {

    CompletableFuture<PatientDTO> getPatient(int patientId);
    CompletableFuture<VisitDTO> getVisit(int visitId);
    CompletableFuture<HokenDTO> getHoken(int visitId);

}
