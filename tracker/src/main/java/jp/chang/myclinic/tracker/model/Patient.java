package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.dto.PatientDTO;

public class Patient {

    private ModelRegistry registry;
    private int patientId;

    public Patient(ModelRegistry registry, PatientDTO dto) {
        this.registry = registry;
        this.patientId = dto.patientId;
    }

}
