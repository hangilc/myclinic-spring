package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.PatientDTO;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PatientRepoInterface {

    int enterPatient(PatientDTO patient);
    PatientDTO getPatient(int patientId);
    void updatePatient(PatientDTO patient);
    Optional<PatientDTO> findPatient(int patientId);
}
