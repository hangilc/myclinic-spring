package jp.chang.myclinic.backend;

import jp.chang.myclinic.backend.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Persistence {

    PatientPersistence getPatientPersistence();
    VisitPersistence getVisitPersistence();
    ShahokokuhoPersistence getShahokokuhoPersistence();
    KoukikoureiPersistence getKoukikoureiPersistence();
    RoujinPersistence getRoujinPersistence();
    KouhiPersistence getKouhiPersistence();
    WqueuePersistence getWqueuePersistence();
    TextPersistence getTextPersistence();
    ShinryouPersistence getShinryouPersistence();
    DrugPersistence getDrugPersistence();
    PracticeLogPersistence getPracticeLogPersistence();

}
