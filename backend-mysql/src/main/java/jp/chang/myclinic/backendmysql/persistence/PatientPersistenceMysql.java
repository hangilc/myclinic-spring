package jp.chang.myclinic.backendmysql.persistence;

import jp.chang.myclinic.backendmysql.entity.core.DTOMapper;
import jp.chang.myclinic.backendmysql.entity.core.Patient;
import jp.chang.myclinic.backendmysql.entity.core.PatientRepository;
import jp.chang.myclinic.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientPersistenceMysql {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DTOMapper mapper;

    public int enterPatient(PatientDTO patientDTO) {
        Patient patient = mapper.fromPatientDTO(patientDTO);
        patient = patientRepository.save(patient);
        return patient.getPatientId();
    }

    public PatientDTO getPatient(int patientId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public Optional<PatientDTO> findPatient(int patientId) {
        throw new RuntimeException("not implemented (api-tool)");
    }

    public void updatePatient(PatientDTO patient) {
        throw new RuntimeException("not implemented (api-tool)");
    }
}
