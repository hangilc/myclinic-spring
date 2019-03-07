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
    private PracticeLogPersistence practiceLogPersistence;

    @Override
    public PracticeLogPersistence getPracticeLogPersistence() {
        return practiceLogPersistence;
    }

    @Autowired
    private TextPersistence textPersistence;

    @Override
    public TextPersistence getTextPersistence() {
        return textPersistence;
    }

    @Autowired
    private KoukikoureiPersistence koukikoureiPersistence;

    @Override
    public KoukikoureiPersistence getKoukikoureiPersistence() {
        return koukikoureiPersistence;
    }

    @Autowired
    private ShinryouPersistence shinryouPersistence;

    @Override
    public ShinryouPersistence getShinryouPersistence() {
        return shinryouPersistence;
    }

    @Autowired
    private DrugPersistence drugPersistence;

    @Override
    public DrugPersistence getDrugPersistence() {
        return drugPersistence;
    }

    @Autowired
    private ShahokokuhoPersistence shahokokuhoPersistence;

    @Override
    public ShahokokuhoPersistence getShahokokuhoPersistence() {
        return shahokokuhoPersistence;
    }

    @Autowired
    private WqueuePersistence wqueuePersistence;

    @Override
    public WqueuePersistence getWqueuePersistence() {
        return wqueuePersistence;
    }

    @Autowired
    private VisitPersistence visitPersistence;

    @Override
    public VisitPersistence getVisitPersistence() {
        return visitPersistence;
    }

    @Autowired
    private KouhiPersistence kouhiPersistence;

    @Override
    public KouhiPersistence getKouhiPersistence() {
        return kouhiPersistence;
    }

    @Autowired
    private RoujinPersistence roujinPersistence;

    @Override
    public RoujinPersistence getRoujinPersistence() {
        return roujinPersistence;
    }
}
