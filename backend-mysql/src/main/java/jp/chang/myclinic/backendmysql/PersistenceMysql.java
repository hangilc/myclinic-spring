package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backend.persistence.PatientPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMysql implements Persistence {

    @Autowired
    private PatientPersistenceMysql patientPersistenceMysql;
    @Override
    public PatientPersistence getPatientPersistence(){
        return patientPersistenceMysql;
    }
}
