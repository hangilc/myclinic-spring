package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.backend.Persistence;
import jp.chang.myclinic.backenddb.tableinterface.ShahokokuhoTableInterface;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DbPersistence implements Persistence {

    private TableSet ts;
    private Query query;
    private SqlTranslator sqlTranslator = new SqlTranslator();

    public DbPersistence(TableSet ts, Query query) {
        this.ts = ts;
        this.query = query;
    }

    protected Query getQuery(){
        return query;
    }

    protected String xlate(String sqlOrig, TableInfo tableInfo) {
        return sqlTranslator.translate(sqlOrig, tableInfo);
    }

    protected String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                           TableInfo tableInfo2, String alias2) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2);
    }

    @Override
    public void enterPatient(PatientDTO patient) {
        ts.patientTable.insert(patient);
    }

    @Override
    public PatientDTO getPatient(int patientId) {
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
        String sql = xlate("select * from Shahokokuho where patientId = ? and " +
                " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.shahokokuhoTable);
        return getQuery().query(sql, ts.shahokokuhoTable, patientId, at, at);
    }

    @Override
    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return ts.shahokokuhoTable.getById(shahokokuhoId);
    }

    @Override
    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        String sql = xlate("select * from Koukikourei where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.koukikoureiTable);
        return getQuery().query(sql, ts.koukikoureiTable, patientId, at, at);
    }

    @Override
    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return ts.koukikoureiTable.getById(koukikoureiId);
    }

    @Override
    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        String sql = xlate("select * from Roujin where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.roujinTable);
        return getQuery().query(sql, ts.roujinTable, patientId, at, at);
    }

    @Override
    public RoujinDTO getRoujin(int roujinId) {
        return ts.roujinTable.getById(roujinId);
    }

    @Override
    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        String sql = xlate("select * from Kouhi where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.kouhiTable);
        return getQuery().query(sql, ts.kouhiTable, patientId, at, at);
    }

    @Override
    public KouhiDTO getKouhi(int kouhiId) {
        return ts.kouhiTable.getById(kouhiId);
    }

    @Override
    public void enterWqueue(WqueueDTO wqueue) {
        ts.wqueueTable.insert(wqueue);
    }

    @Override
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(ts.shinryouAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    @Override
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(ts.drugAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    @Override
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitIds.stream().map(ts.shoukiTable::getById).filter(Objects::nonNull).collect(toList());
    }

    @Override
    public void enterText(TextDTO text) {
        ts.textTable.insert(text);
    }

    @Override
    public TextDTO getText(int textId) {
        return ts.textTable.getById(textId);
    }

    @Override
    public void updateText(TextDTO text) {
        ts.textTable.update(text);
    }

    @Override
    public void deleteText(int textId) {
        ts.textTable.delete(textId);
    }

    @Override
    public List<TextDTO> listText(int visitId) {
        String sql = xlate("select * from Text where visitId = ? order by textId",
                ts.textTable);
        return getQuery().query(sql, ts.textTable, visitId);
    }

    @Override
    public void enterPracticeLog(PracticeLogDTO practiceLog) {
        ts.practiceLogTable.insert(practiceLog);
    }

}
