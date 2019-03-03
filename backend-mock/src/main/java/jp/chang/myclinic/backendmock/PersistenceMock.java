package jp.chang.myclinic.backendmock;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backend.persistence.*;
import jp.chang.myclinic.backendmock.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceMock implements Persistence {

    private PatientPersistence patientPersistence = new PatientPersistenceMock();
    @Override
    public PatientPersistence getPatientPersistence() {
        return patientPersistence;
    }

    private VisitPersistence visitPersistence = new VisitPersistenceMock();
    @Override
    public VisitPersistence getVisitPersistence() {
        return visitPersistence;
    }

    private ShahokokuhoPersistence shahokokuhoPersistence = new ShahokokuhoPersistenceMock();
    @Override
    public ShahokokuhoPersistence getShahokokuhoPersistence() {
        return shahokokuhoPersistence;
    }

    private KoukikoureiPersistence koukikoureiPersistence = new KoukikoureiPersistenceMock();
    @Override
    public KoukikoureiPersistence getKoukikoureiPersistence() {
        return koukikoureiPersistence;
    }

    private RoujinPersistence roujinPersistence = new RoujinPersistenceMock();
    @Override
    public RoujinPersistence getRoujinPersistence() {
        return roujinPersistence;
    }

    private KouhiPersistence kouhiPersistence = new KouhiPersistenceMock();
    @Override
    public KouhiPersistence getKouhiPersistence() {
        return kouhiPersistence;
    }

    private WqueuePersistence wqueuePersistence = new WqueuePersistenceMock();
    @Override
    public WqueuePersistence getWqueuePersistence() {
        return wqueuePersistence;
    }

    private TextPersistence textPersistence = new TextPersistenceMock();
    @Override
    public TextPersistence getTextPersistence() {
        return textPersistence;
    }

    private ShinryouPersistence shinryouPersistence = new ShinryouPersistenceMock();
    @Override
    public ShinryouPersistence getShinryouPersistence() {
        return shinryouPersistence;
    }

    private DrugPersistence drugPersistence = new DrugPersistenceMock();
    @Override
    public DrugPersistence getDrugPersistence() {
        return drugPersistence;
    }
}
