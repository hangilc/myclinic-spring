package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backend.persistence.PatientPersistence;
import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class PatientPersistenceMysql implements PatientPersistence {

    @Override
    public int enterPatient(PatientDTO patient) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
