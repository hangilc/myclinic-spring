package jp.chang.myclinic.backendpgsql.persistence;

import jp.chang.myclinic.backend.persistence.PatientPersistence;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.Optional;

public class PatientPersistencePgsql implements PatientPersistence {

    private PatientTable table = new PatientTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        table.insert(patient);
        return patient.patientId;
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return table.getById(patientId);
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        table.update(patient);
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        return Optional.ofNullable(getPatient(patientId));
    }

}
