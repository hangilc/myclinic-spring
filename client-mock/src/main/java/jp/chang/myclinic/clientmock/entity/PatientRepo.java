package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatientRepo implements PatientRepoInterface {

    private int serialId = 1;
    private Map<Integer, PatientDTO> registry = new HashMap<>();

    @Override
    public int enterPatient(PatientDTO patient){
        patient.patientId = serialId++;
        registry.put(patient.patientId, patient);
        return patient.patientId;
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return registry.get(patientId);
    }

    @Override
    public void updatePatient(PatientDTO patient) {
        if( !registry.containsKey(patient.patientId) ){
            throw new RuntimeException("no such patient");
        }
        registry.put(patient.patientId, patient);
    }

    @Override
    public Optional<PatientDTO> findPatient(int patientId) {
        return Optional.ofNullable(registry.getOrDefault(patientId, null));
    }
}
