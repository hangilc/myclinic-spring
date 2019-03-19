package jp.chang.myclinic.backendmock;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backendmock.persistence.*;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.util.List;

public class PersistenceMock implements Persistence {

    private PatientPersistenceMock patientPersistence = new PatientPersistenceMock();
    private VisitPersistenceMock visitPersistence = new VisitPersistenceMock();
    private ShahokokuhoPersistenceMock shahokokuhoPersistence = new ShahokokuhoPersistenceMock();
    private KoukikoureiPersistenceMock koukikoureiPersistence = new KoukikoureiPersistenceMock();
    private RoujinPersistenceMock roujinPersistence = new RoujinPersistenceMock();
    private KouhiPersistenceMock kouhiPersistence = new KouhiPersistenceMock();
    private WqueuePersistenceMock wqueuePersistence = new WqueuePersistenceMock();
    private TextPersistenceMock textPersistence = new TextPersistenceMock();
    private ShinryouPersistenceMock shinryouPersistence = new ShinryouPersistenceMock();
    private DrugPersistenceMock drugPersistence = new DrugPersistenceMock();
    private PracticeLogPersistenceMock practiceLogPersistence = new PracticeLogPersistenceMock();


    @Override
    public void enterPatient(PatientDTO patient) {
        patientPersistence.enterPatient(patient);
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return patientPersistence.getPatient(patientId);
    }

    @Override
    public void enterVisit(VisitDTO visit) {
        visitPersistence.enterVisit(visit);
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        return visitPersistence.getVisit(visitId);
    }

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        return shahokokuhoPersistence.findAvailableShahokokuho(patientId, at);
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return shahokokuhoPersistence.getShahokokuho(shahokokuhoId);
    }

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        return koukikoureiPersistence.findAvailableKoukikourei(patientId, at);
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return koukikoureiPersistence.getKoukikourei(koukikoureiId);
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        return roujinPersistence.findAvailableRoujin(patientId, at);
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        return roujinPersistence.getRoujin(roujinId);
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        return kouhiPersistence.findAvailableKouhi(patientId, at);
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return kouhiPersistence.getKouhi(kouhiId);
    }

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        wqueuePersistence.enterWqueue(wqueue);
    }

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouPersistence.batchGetShinryouAttr(shinryouIds);
    }

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugPersistence.batchGetDrugAttr(drugIds);
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitPersistence.batchGetShouki(visitIds);
    }

    @Override
    public void enterText(TextDTO text) {
        textPersistence.enterText(text);
    }

    @Override
    public TextDTO getText(int textId) {
        return textPersistence.getText(textId);
    }

    @Override
    public void updateText(TextDTO text) {
        textPersistence.updateText(text);
    }

    @Override
    public void deleteText(int textId) {
        textPersistence.deleteText(textId);
    }

    @Override
    public List<TextDTO> listText(int visitId) {
        return textPersistence.listText(visitId);
    }

    @Override
    public void enterPracticeLog(PracticeLogDTO practiceLog) {
        practiceLogPersistence.enterPracticeLog(practiceLog);
    }
}
