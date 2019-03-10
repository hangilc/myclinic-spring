package jp.chang.myclinic.backendpgsql.persistence;

import jp.chang.myclinic.backend.persistence.PatientPersistence;
import jp.chang.myclinic.backendpgsql.DB;
import jp.chang.myclinic.backendpgsql.PersistenceBase;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.Connection;
import java.util.Optional;

public class PatientPersistencePgsql extends PersistenceBase implements PatientPersistence {

    private PatientTable table = new PatientTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        return with(conn -> { table.insert(conn, patient); return patient.patientId; });
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return with(conn -> table.getById(conn, patientId));
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        execWith(conn -> table.update(conn, patient));
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        return Optional.ofNullable(getPatient(patientId));
    }

}
