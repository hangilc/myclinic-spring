package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backend.persistence.*;
import jp.chang.myclinic.backendmysql.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMysql implements Persistence {

    @Autowired
    private PatientPersistenceMysql patientPersistenceMysql;

    @Override
    public PatientPersistence getPatientPersistence() {
        return patientPersistenceMysql;
    }

    @Autowired
    private PracticeLogPersistenceMysql practiceLogPersistenceMysql;

    @Override
    public PracticeLogPersistence getPracticeLogPersistence() {
        return practiceLogPersistenceMysql;
    }

    @Autowired
    private TextPersistenceMysql textPersistenceMysql;

    @Override
    public TextPersistence getTextPersistence() {
        return textPersistenceMysql;
    }

    @Autowired
    private KoukikoureiPersistenceMysql koukikoureiPersistenceMysql;

    @Override
    public KoukikoureiPersistence getKoukikoureiPersistence() {
        return koukikoureiPersistenceMysql;
    }

    @Autowired
    private ShinryouPersistenceMysql shinryouPersistenceMysql;

    @Override
    public ShinryouPersistence getShinryouPersistence() {
        return shinryouPersistenceMysql;
    }

    @Autowired
    private DrugPersistenceMysql drugPersistenceMysql;

    @Override
    public DrugPersistence getDrugPersistence() {
        return drugPersistenceMysql;
    }

    @Autowired
    private ShahokokuhoPersistenceMysql shahokokuhoPersistenceMysql;

    @Override
    public ShahokokuhoPersistence getShahokokuhoPersistence() {
        return shahokokuhoPersistenceMysql;
    }

    @Autowired
    private WqueuePersistenceMysql wqueuePersistenceMysql;

    @Override
    public WqueuePersistence getWqueuePersistence() {
        return wqueuePersistenceMysql;
    }

    @Autowired
    private VisitPersistenceMysql visitPersistenceMysql;

    @Override
    public VisitPersistence getVisitPersistence() {
        return visitPersistenceMysql;
    }

    @Autowired
    private KouhiPersistenceMysql kouhiPersistenceMysql;

    @Override
    public KouhiPersistence getKouhiPersistence() {
        return kouhiPersistenceMysql;
    }

    @Autowired
    private RoujinPersistenceMysql roujinPersistenceMysql;

    @Override
    public RoujinPersistence getRoujinPersistence() {
        return roujinPersistenceMysql;
    }
}
