package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.Shuushokugo;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

public class Backend {

    private TableSet ts;
    private Query query;
    private SqlTranslator sqlTranslator = new SqlTranslator();
    private PracticeLogger practiceLogger;
    private HotlineLogger hotlineLogger;

    public Backend(TableSet ts, Query query){
        this.ts = ts;
        this.query = query;
        this.practiceLogger = new PracticeLogger();
        practiceLogger.setSaver(this::enterPracticeLog);
        this.hotlineLogger = new HotlineLogger();
    }

    public void setPracticeLogPublisher(Consumer<String> publisher){
        practiceLogger.setPublisher(publisher::accept);
    }

    public void setHotlineLogPublisher(Consumer<String> publisher){
        hotlineLogger.setHotlineLogPublisher(publisher::accept);
    }

    public Query getQuery(){
        return query;
    }

    public String xlate(String sqlOrig, TableInfo tableInfo) {
        return sqlTranslator.translate(sqlOrig, tableInfo);
    }

    public String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                         TableInfo tableInfo2, String alias2) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2);
    }

    public void enterPatient(PatientDTO patient){
        ts.patientTable.insert(patient);
        practiceLogger.logPatientCreated(patient);
    }

    public PatientDTO getPatient(int patientId){
        return ts.patientTable.getById(patientId);
    }

    public void updatePatient(PatientDTO patient){
        PatientDTO prev = getPatient(patient.patientId);
        ts.patientTable.update(patient);
        practiceLogger.logPatientUpdated(prev, patient);
    }

    public List<PatientDTO> searchPatient(String text){
        throw new RuntimeException("not implemented");
    }

    private void enterVisit(VisitDTO visit){
        ts.visitTable.insert(visit);
        practiceLogger.logVisitCreated(visit);
    }

    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        String sql = xlate("select * from Shahokokuho where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.shahokokuhoTable);
        return getQuery().query(sql, ts.shahokokuhoTable, patientId, at, at);
    }

    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        String sql = xlate("select * from Koukikourei where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.koukikoureiTable);
        return getQuery().query(sql, ts.koukikoureiTable, patientId, at, at);
    }

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        String sql = xlate("select * from Roujin where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.roujinTable);
        return getQuery().query(sql, ts.roujinTable, patientId, at, at);
    }

    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        String sql = xlate("select * from Kouhi where patientId = ? and " +
                        " validFrom <= date(?) and (validUpto is null or validUpto >= date(?))",
                ts.kouhiTable);
        return getQuery().query(sql, ts.kouhiTable, patientId, at, at);
    }

    public VisitDTO startVisit(int patientId, LocalDateTime at){
        LocalDate atDate = at.toLocalDate();
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.patientId = patientId;
        visitDTO.visitedAt = DateTimeUtil.toSqlDateTime(at);
        {
            List<ShahokokuhoDTO> list = findAvailableShahokokuho(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.shahokokuhoId = 0;
            } else {
                visitDTO.shahokokuhoId = list.get(0).shahokokuhoId;
            }
        }
        {
            List<KoukikoureiDTO> list = findAvailableKoukikourei(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.koukikoureiId = 0;
            } else {
                visitDTO.koukikoureiId = list.get(0).koukikoureiId;
            }
        }
        {
            List<RoujinDTO> list = findAvailableRoujin(patientId, atDate);
            if (list.size() == 0) {
                visitDTO.roujinId = 0;
            } else {
                visitDTO.roujinId = list.get(0).roujinId;
            }
        }
        {
            visitDTO.kouhi1Id = 0;
            visitDTO.kouhi2Id = 0;
            visitDTO.kouhi3Id = 0;
            List<KouhiDTO> list = findAvailableKouhi(patientId, atDate);
            int n = list.size();
            if (n > 0) {
                visitDTO.kouhi1Id = list.get(0).kouhiId;
                if (n > 1) {
                    visitDTO.kouhi2Id = list.get(1).kouhiId;
                    if (n > 2) {
                        visitDTO.kouhi3Id = list.get(2).kouhiId;
                    }
                }
            }
        }
        enterVisit(visitDTO);
        WqueueDTO wqueueDTO = new WqueueDTO();
        wqueueDTO.visitId = visitDTO.visitId;
        wqueueDTO.waitState = MyclinicConsts.WqueueStateWaitExam;
        enterWqueue(wqueueDTO);
        return visitDTO;
    }

    public void startExam(int visitId){
        throw new RuntimeException("not implemented");
    }

    public void suspendExam(int visitId){
        throw new RuntimeException("not implemented");
    }

    public void endExam(int visitId, int charge){
        throw new RuntimeException("not implemented");
    }

    private void enterWqueue(WqueueDTO wqueue){
        ts.wqueueTable.insert(wqueue);
        practiceLogger.logWqueueCreated(wqueue);
    }

    public HokenDTO getHoken(int visitId) {
        VisitDTO visitDTO = getVisit(visitId);
        HokenDTO hokenDTO = new HokenDTO();
        if (visitDTO.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = getShahokokuho(visitDTO.shahokokuhoId);
        }
        if (visitDTO.koukikoureiId > 0) {
            hokenDTO.koukikourei = getKoukikourei(visitDTO.koukikoureiId);
        }
        if (visitDTO.roujinId > 0) {
            hokenDTO.roujin = getRoujin(visitDTO.roujinId);
        }
        if (visitDTO.kouhi1Id > 0) {
            hokenDTO.kouhi1 = getKouhi(visitDTO.kouhi1Id);
        }
        if (visitDTO.kouhi2Id > 0) {
            hokenDTO.kouhi2 = getKouhi(visitDTO.kouhi2Id);
        }
        if (visitDTO.kouhi3Id > 0) {
            hokenDTO.kouhi3 = getKouhi(visitDTO.kouhi3Id);
        }
        return hokenDTO;
    }

    public HokenDTO listAvailableHoken(int patientId, LocalDate visitedAt){
        throw new RuntimeException("not implemented");
    }

    public void updateHoken(VisitDTO visit){
        throw new RuntimeException("not implemented");
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(ts.drugAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public VisitDTO getVisit(int visitId){
        return ts.visitTable.getById(visitId);
    }

    public void deleteVisit(int visitId){
        throw new RuntimeException("not implemented");
    }

    public List<VisitPatientDTO> listRecentVisitWithPatient(int page, int itemsPerPage){
        String sql = "select v.*, p.* from Visit v, Patient p where v.patientId = p.patientId " +
                " order by v.visitId desc limit ? offset ? ";
        sql = xlate(sql, ts.visitTable, "v", ts.patientTable, "p");
        return getQuery().query(sql,
                (rs, ctx) -> {
                    VisitPatientDTO vp = new VisitPatientDTO();
                    vp.visit = ts.visitTable.project(rs, ctx);
                    vp.patient = ts.patientTable.project(rs, ctx);
                    return vp;
                },
                itemsPerPage,
                page * itemsPerPage);
    }

    public List<VisitPatientDTO> listTodaysVisit(){
        throw new RuntimeException("not implemented");
    }

    public VisitFull2PageDTO listVisitFull2(int patientId, int page){
        throw new RuntimeException("not implemented");
    }

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitIds.stream().map(ts.shoukiTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public void updateShouki(ShoukiDTO shouki){
        throw new RuntimeException("not implemented");
    }

    public void deleteShouki(int visitId){
        throw new RuntimeException("not implemented");
    }

    public void enterText(TextDTO text){
        ts.textTable.insert(text);
        practiceLogger.logTextCreated(text);
    }

    public TextDTO getText(int textId){
        return ts.textTable.getById(textId);
    }

    public void updateText(TextDTO text){
        TextDTO prev = getText(text.textId);
        ts.textTable.update(text);
        practiceLogger.logTextUpdated(prev, text);
    }

    public void deleteText(int textId){
        TextDTO text = getText(textId);
        ts.textTable.delete(textId);
        practiceLogger.logTextDeleted(text);
    }

    public List<TextDTO> listText(int visitId) {
        String sql = xlate("select * from Text where visitId = ? order by textId",
                ts.textTable);
        return getQuery().query(sql, ts.textTable, visitId);
    }

    public TextVisitPageDTO searchTextByPage(int patientId, String text, int page){
        throw new RuntimeException("not implemented");
    }

    public TextVisitPatientPageDTO searchTextGlobally(String text, int page){
        throw new RuntimeException("not implemented");
    }

    public void enterDrug(DrugDTO drug){
        throw new RuntimeException("not implemented");
    }

    public void updateDrug(DrugDTO drug){
        throw new RuntimeException("not implemented");
    }

    public void batchUpdateDrugDays(List<Integer> drugIds, int days){
        throw new RuntimeException("not implemented");
    }

    public void deleteDrug(int drugId){
        throw new RuntimeException("not implemented");
    }

    public void batchDeleteDrugs(List<Integer> drugIds){
        throw new RuntimeException("not implemented");
    }

    public DrugFullDTO getDrugFull(int drugId){
        throw new RuntimeException("not implemented");
    }

    public List<DrugFullDTO> listDrugFull(int visitId){
        throw new RuntimeException("not implemented");
    }

    public void enterShinryou(ShinryouDTO shinryou){
        throw new RuntimeException("not implemented");
    }

    public void deleteShinryou(int shinryouId){
        throw new RuntimeException("not implemented");
    }

    public ShinryouFullDTO getShinryouFull(int shinryouId){
        throw new RuntimeException("not implemented");
    }

    public BatchEnterResultDTO batchEnterShinryouByName(List<String> names, int visitId){
        throw new RuntimeException("not implemented");
    }

    public List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds){
        throw new RuntimeException("not implemented");
    }

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(ts.shinryouAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public ShinryouAttrDTO getShinryouAttr(int shinryouId){
        throw new RuntimeException("not implemented");
    }

    public void enterShinryouAttr(ShinryouAttrDTO shinryou){
        throw new RuntimeException("not implemented");
    }

    public ConductFullDTO enterConductFull(ConductEnterRequestDTO req){
        throw new RuntimeException("not implemented");
    }

    public void delteConduct(int conductId){
        throw new RuntimeException("not implemented");
    }

    public void modifyConductKind(int conductId, int conductKind){
        throw new RuntimeException("not implemented");
    }

    public List<ConductFullDTO> listConductFullByIds(List<Integer> conductIds){
        throw new RuntimeException("not implemented");
    }

    public void deleteConductShinryou(int conductShinryouId){
        throw new RuntimeException("not implemented");
    }

    public void deleteConductDrug(int conductDrugId){
        throw new RuntimeException("not implemented");
    }

    public void deleteConductKizai(int conductKizaiId){
        throw new RuntimeException("not implemented");
    }

    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId){
        return ts.shahokokuhoTable.getById(shahokokuhoId);
    }

    public KoukikoureiDTO getKoukikourei(int koukikoureiId){
        return ts.koukikoureiTable.getById(koukikoureiId);
    }

    public RoujinDTO getRoujin(int roujinId){
        return ts.roujinTable.getById(roujinId);
    }

    public KouhiDTO getKouhi(int kouhiId){
        return ts.kouhiTable.getById(kouhiId);
    }

    public List<DiseaseFullDTO> listCurrentDiseaseFull(int patientId){
        throw new RuntimeException("not implemented");
    }

    public List<DiseaseFullDTO> listDiseaseFull(int patientId){
        throw new RuntimeException("not implemented");
    }

    public MeisaiDTO getMeisai(int visitId){
        throw new RuntimeException("not implemented");
    }

    public List<ShinryouMasterDTO> searchShinryouMaster(String text, LocalDate at){
        throw new RuntimeException("not implemented");
    }

    public List<IyakuhinMasterDTO> searchIyakuhinMaster(String text, LocalDate at){
        throw new RuntimeException("not implemented");
    }

    public List<KizaiMasterDTO> searchKizaiMaster(String text, LocalDate at){
        throw new RuntimeException("not implemented");
    }

    public List<ByoumeiMasterDTO> searchByoumeiMaster(String text, LocalDate at){
        throw new RuntimeException("not implemented");
    }

    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(String text, LocalDate at){
        throw new RuntimeException("not implemented");
    }

    private void enterPracticeLog(PracticeLogDTO practiceLog){
        ts.practiceLogTable.insert(practiceLog);
    }

}
