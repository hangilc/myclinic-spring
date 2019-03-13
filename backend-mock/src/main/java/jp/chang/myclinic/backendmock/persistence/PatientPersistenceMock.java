package jp.chang.myclinic.backendmock.persistence;

import jp.chang.myclinic.dto.PatientDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatientPersistenceMock {

    private int serialId = 1;
    private Map<Integer, PatientDTO> registry = new HashMap<>();

    public int enterPatient(PatientDTO patient){
        patient.patientId = serialId++;
        registry.put(patient.patientId, patient);
        return patient.patientId;
    }

    public PatientDTO getPatient(int patientId) {
        return registry.get(patientId);
    }

    public void updatePatient(PatientDTO patient) {
        if( !registry.containsKey(patient.patientId) ){
            throw new RuntimeException("no such patient");
        }
        registry.put(patient.patientId, patient);
    }

    public Optional<PatientDTO> findPatient(int patientId) {
        return Optional.ofNullable(registry.getOrDefault(patientId, null));
    }

}
