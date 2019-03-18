package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backenddb.tableinterface.ShahokokuhoTableInterface;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class DbPersistence implements Persistence {

    private TableSet ts;

    public DbPersistence(TableSet ts) {
        this.ts = ts;
    }

    @Override
    public void enterPatient(PatientDTO patient){
        ts.patientTable.insert(patient);
    }

    @Override
    public PatientDTO getPatient(int patientId){
        return ts.patientTable.getById(patientId);
    }

    @Override
    public void enterVisit(VisitDTO visit) {
        ts.visitTable.insert(visit);
    }

    @Override
    public VisitDTO getVisit(int visitId) {
        return ts.visitTable.getById(visitId);
    }


    @Override
    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        ShahokokuhoTableInterface h = ts.shahokokuhoTable;
        String sql = "select * from " + h.getTableName() + " h " +
                " where h." + h.patientId() + " = ? and " +
                " h.valid_from <= date(?) and  (h.valid_upto is null or h.valid_upto >= date(?)) ";
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
    public void enterText(TextDTO text) {
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

    public void updatePatient(PatientDTO patient){
        ts.patientTable.update(patient);
    }

}
