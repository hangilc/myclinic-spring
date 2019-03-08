package jp.chang.myclinic.backendpgsql.persistence;

import jp.chang.myclinic.backend.persistence.PatientPersistence;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.Optional;

public class PatientPersistencePgsql implements PatientPersistence {

    private PatientTable table = new PatientTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }
}
