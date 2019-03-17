package jp.chang.myclinic.backendpgsql;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backendpgsql.table.PatientTable;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class PgsqlPersistence implements Persistence {

    private PatientTable patientTable = new PatientTable();

    @Override
    public int enterPatient(PatientDTO patient) {
        patientTable.insert(patient);
        return patient.patientId;
    }

    @Override
    public PatientDTO getPatient(int patientId) {
        return patientTable.getById(patientId);
    }

    @Override
    public int enterVisit(VisitDTO visit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int enterText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public TextDTO getText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void updateText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void deleteText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<TextDTO> listText(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void enterPracticeLog(PracticeLogDTO practiceLog) {
        throw new RuntimeException("not implemented");
    }

}
