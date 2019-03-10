package jp.chang.myclinic.backendpgsql.persistence;

import jp.chang.myclinic.backend.persistence.PatientPersistence;
import jp.chang.myclinic.backendpgsql.DB;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.PatientDTO;

import java.sql.Connection;
import java.util.Optional;

public class PatientPersistencePgsql implements PatientPersistence {

    private PatientTable table = new PatientTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        return DB.get(conn -> enterPatient(conn, patient));
    }

    public int enterPatient(Connection conn, PatientDTO patient) {
        table.insert(conn, patient);
        return patient.patientId;
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return DB.get(conn -> getPatient(conn, patientId));
    }

    public PatientDTO getPatient(Connection conn, int patientId) {
        //return table.getById(conn, patientId);
        throw new RuntimeException("not implemented");
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        return DB.get(conn -> findPatient(patientId));
    }

    public Optional<PatientDTO> findPatient(Connection conn, int patientId) {
        return Optional.ofNullable(getPatient(conn, patientId));
    }
}
