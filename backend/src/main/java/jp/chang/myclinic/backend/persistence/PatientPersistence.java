package jp.chang.myclinic.backend.persistence;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PatientPersistence {

    int enterPatient(PatientDTO patient);
    PatientDTO getPatient(int patientId);

}
