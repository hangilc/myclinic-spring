package jp.chang.myclinic.backendmysql;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backendmysql.persistence.*;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PersistenceMysql implements Persistence {

    @Autowired
    private PatientPersistenceMysql patientPersistence;

    @Autowired
    private PracticeLogPersistenceMysql practiceLogPersistence;

    @Autowired
    private TextPersistenceMysql textPersistence;

    @Autowired
    private KoukikoureiPersistenceMysql koukikoureiPersistence;

    @Autowired
    private ShinryouPersistenceMysql shinryouPersistence;

    @Autowired
    private DrugPersistenceMysql drugPersistence;

    @Autowired
    private ShahokokuhoPersistenceMysql shahokokuhoPersistence;

    @Autowired
    private WqueuePersistenceMysql wqueuePersistence;

    @Autowired
    private VisitPersistenceMysql visitPersistence;

    @Autowired
    private KouhiPersistenceMysql kouhiPersistence;

    @Autowired
    private RoujinPersistenceMysql roujinPersistence;

    @Override
    public int enterPatient(PatientDTO patient) {
        return patientPersistence.enterPatient(patient);
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return patientPersistence.getPatient(patientId);
    }

    @Override
    public int enterVisit(VisitDTO visit) {
        return visitPersistence.enterVisit(visit);
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
    public int enterText(TextDTO text) {
        return textPersistence.enterText(text);
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
