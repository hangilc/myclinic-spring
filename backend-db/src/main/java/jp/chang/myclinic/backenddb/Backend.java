package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.PharmaQueueState;
import jp.chang.myclinic.consts.Shuushokugo;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import static jp.chang.myclinic.backenddb.Query.Projector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    private static <S,T,U> Projector<U> biProjector(Projector<S> p1, Projector<T> p2, BiFunction<S,T,U> f){
        return (rs, ctx) -> {
            S s = p1.project(rs, ctx);
            T t = p2.project(rs, ctx);
            return f.apply(s, t);
        };
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

    public String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                         TableInfo tableInfo2, String alias2, TableInfo tableInfo3, String alias3) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2,
                tableInfo3, alias3);
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

    public List<PatientDTO> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword){
        String sql = xlate("select * from Patient where " +
                        " (lastName like ? or lastNameYomi like ?) and " +
                        " (firstName like ? or firstNameYomi like ?) ",
                ts.patientTable);
        return getQuery().query(sql, ts.patientTable,
                lastNameKeyword, lastNameKeyword, firstNameKeyword, firstNameKeyword);
    }

    public List<PatientDTO> searchPatientByKeyword(String keyword){
        String sql = xlate("select * from Patient where " +
                        " (lastName like ? or lastNameYomi like ?) or " +
                        " (firstName like ? or firstNameYomi like ?) ",
                ts.patientTable);
        return getQuery().query(sql, ts.patientTable,
                keyword, keyword, keyword, keyword);
    }

    public List<PatientDTO> searchPatient(String text){
        text = text.trim();
        if( text.isEmpty() ){
            return Collections.emptyList();
        }
        String[] parts = text.split("\\s+", 2);
        if( parts.length == 1 ){
            String s = "%" + text + "%";
            return searchPatientByKeyword(s);
        } else {
            String last = "%" + parts[0] + "%";
            String first = "%"+ parts[1] + "%";
            return searchPatientByKeyword(last, first);
        }
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

    // Visit ////////////////////////////////////////////////////////////////////////

    private void enterVisit(VisitDTO visit){
        ts.visitTable.insert(visit);
        practiceLogger.logVisitCreated(visit);
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
        changeWqueueState(visitId, WqueueWaitState.InExam.getCode());
    }

    public void suspendExam(int visitId){
        changeWqueueState(visitId, WqueueWaitState.WaitReExam.getCode());
    }

    private boolean isTodaysVisit(VisitDTO visit){
        return visit.visitedAt.substring(0, 10).equals(LocalDate.now().toString());
    }

    public void endExam(int visitId, int charge){
        VisitDTO visit = ts.visitTable.getById(visitId);
        if( visit == null ){
            throw new RuntimeException("No such visit: " + visitId);
        }
        boolean isToday = isTodaysVisit(visit);
        setChargeOfVisit(visitId, charge);
        WqueueDTO wqueue = ts.wqueueTable.getById(visitId);
        if (wqueue != null && isToday) {
            changeWqueueState(visitId, WqueueWaitState.WaitCashier.getCode());
        } else {
            if(wqueue != null ){ // it not today
                deleteWqueue(visitId);
            }
            WqueueDTO newWqueue = new WqueueDTO();
            newWqueue.visitId = visitId;
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            enterWqueue(newWqueue);
        }
        PharmaQueueDTO pharmaQueue = getPharmaQueue(visitId);
        if( pharmaQueue != null ){
            deletePharmaQueue(visitId);
        }
        if (isToday) {
            int unprescribed = countUnprescribedDrug(visitId);
            if (unprescribed > 0) {
                PharmaQueueDTO newPharmaQueue = new PharmaQueueDTO();
                newPharmaQueue.visitId = visitId;
                newPharmaQueue.pharmaState = PharmaQueueState.WaitPack.getCode();
                ts.pharmaQueueTable.insert(newPharmaQueue);
                practiceLogger.logPharmaQueueCreated(newPharmaQueue);
            }
        }
    }

    // Charge /////////////////////////////////////////////////////////////////////////////

    public void enterCharge(ChargeDTO charge){
        ts.chargeTable.insert(charge);
        practiceLogger.logChargeCreated(charge);
    }

    public void setChargeOfVisit(int visitId, int charge) {
        ChargeDTO prev = ts.chargeTable.getById(visitId);
        if ( prev != null ) {
            ChargeDTO updated = ChargeDTO.copy(prev);
            ts.chargeTable.update(updated);
            practiceLogger.logChargeUpdated(prev, updated);
        } else {
            ChargeDTO newCharge = new ChargeDTO();
            newCharge.visitId = visitId;
            newCharge.charge = charge;
            enterCharge(newCharge);
        }
    }

    public ChargeDTO getCharge(int visitId){
        return ts.chargeTable.getById(visitId);
    }

    // Wqueue /////////////////////////////////////////////////////////////////////////////

    private void enterWqueue(WqueueDTO wqueue){
        ts.wqueueTable.insert(wqueue);
        practiceLogger.logWqueueCreated(wqueue);
    }

    public WqueueDTO getWqueue(int visitId){
        return ts.wqueueTable.getById(visitId);
    }

    private void changeWqueueState(int visitId, int state) {
        WqueueDTO prev = ts.wqueueTable.getById(visitId);
        WqueueDTO updated = WqueueDTO.copy(prev);
        updated.waitState = state;
        ts.wqueueTable.update(updated);
        practiceLogger.logWqueueUpdated(prev, updated);
    }

    public void deleteWqueue(int visitId) {
        WqueueDTO wqueue = ts.wqueueTable.getById(visitId);
        if( wqueue == null ){
            return;
        }
        ts.wqueueTable.delete(visitId);
        practiceLogger.logWqueueDeleted(wqueue);
    }

    public List<WqueueDTO> listWqueue(){
        String sql = xlate("select * from Wqueue order by visitId", ts.wqueueTable);
        return getQuery().query(sql, ts.wqueueTable);
    }

    private WqueueFullDTO composeWqueueFullDTO(WqueueDTO wqueue){
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = wqueue;
        wqueueFullDTO.visit = getVisit(wqueue.visitId);
        wqueueFullDTO.patient = getPatient(wqueueFullDTO.visit.patientId);
        return wqueueFullDTO;
    }

    public List<WqueueFullDTO> listWqueueFull(){
        return listWqueue().stream().map(this::composeWqueueFullDTO).collect(toList());
    }


    // Hoken //////////////////////////////////////////////////////////////////////////////

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
        HokenDTO hokenDTO = new HokenDTO();
        hokenDTO.shahokokuho = findAvailableShahokokuho(patientId, visitedAt).stream().findFirst().orElse(null);
        hokenDTO.koukikourei = findAvailableKoukikourei(patientId, visitedAt).stream().findFirst().orElse(null);
        hokenDTO.roujin = findAvailableRoujin(patientId, visitedAt).stream().findFirst().orElse(null);
        List<KouhiDTO> kouhiList = findAvailableKouhi(patientId, visitedAt);
        if (kouhiList.size() > 0) {
            hokenDTO.kouhi1 = kouhiList.get(0);
            if (kouhiList.size() > 1) {
                hokenDTO.kouhi2 = kouhiList.get(1);
                if (kouhiList.size() > 2) {
                    hokenDTO.kouhi3 = kouhiList.get(2);
                }
            }
        }
        return hokenDTO;
    }

    public void updateHoken(VisitDTO visit){
        VisitDTO origVisit = ts.visitTable.getById(visit.visitId);
        origVisit.shahokokuhoId = visit.shahokokuhoId;
        origVisit.koukikoureiId = visit.koukikoureiId;
        origVisit.roujinId = visit.roujinId;
        origVisit.kouhi1Id = visit.kouhi1Id;
        origVisit.kouhi2Id = visit.kouhi2Id;
        origVisit.kouhi3Id = visit.kouhi3Id;
        updateVisit(origVisit);
    }

    // Drug ///////////////////////////////////////////////////////////////////////////

    public DrugAttrDTO getDrugAttr(int drugId){
        return ts.drugAttrTable.getById(drugId);
    }

    public void enterDrugAttr(DrugAttrDTO drugAttr){
        ts.drugAttrTable.insert(drugAttr);
    }

    private void updateDrugAttr(DrugAttrDTO drugAttr){
        ts.drugAttrTable.update(drugAttr);
    }

    private void deleteDrugAttr(int drugId){
        ts.drugAttrTable.delete(drugId);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(ts.drugAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public DrugAttrDTO setDrugTekiyou(int drugId, String tekiyou){
        DrugAttrDTO attr = ts.drugAttrTable.getById(drugId);
        if( attr != null ){
            attr.tekiyou = tekiyou;
            updateDrugAttr(attr);
            return attr;
        } else {
            DrugAttrDTO newDrugAttr = new DrugAttrDTO();
            newDrugAttr.drugId = drugId;
            newDrugAttr.tekiyou = tekiyou;
            enterDrugAttr(newDrugAttr);
            return newDrugAttr;
        }
    }

    public void deleteDrugTekiyou(int drugId){
        DrugAttrDTO attr = ts.drugAttrTable.getById(drugId);
        if( attr == null ){
            return;
        }
        attr.tekiyou = null;
        if (DrugAttrDTO.isEmpty(attr)) {
            deleteDrugAttr(drugId);
        } else {
            updateDrugAttr(attr);
        }
    }

    public int countUnprescribedDrug(int visitId){
        String sql = xlate("select count(*) from Drug where visitId = ? and prescribed = 0",
                ts.drugTable);
        return getQuery().get(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), visitId);
    }

    // Visit ////////////////////////////////////////////////////////////////////////////

    public VisitDTO getVisit(int visitId){
        return ts.visitTable.getById(visitId);
    }

    private void updateVisit(VisitDTO visit){
        VisitDTO prev = getVisit(visit.visitId);
        ts.visitTable.update(visit);
        practiceLogger.logVisitUpdated(prev,visit);
    }

    public void deleteVisitSafely(){
        throw new RuntimeException("not implemented");
    }

    private void deleteVisit(int visitId){
        VisitDTO visit = ts.visitTable.getById(visitId);
        ts.visitTable.delete(visitId);
        practiceLogger.logVisitDeleted(visit);
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

    private VisitFullDTO getVisitFull(VisitDTO visitDTO) {
        throw new RuntimeException("not implemented");
//        int visitId = visitDTO.visitId;
//        VisitFullDTO visitFullDTO = new VisitFullDTO();
//        visitFullDTO.visit = visitDTO;
//        visitFullDTO.texts = listText(visitId);
//        visitFullDTO.shinryouList = listShinryouFull(visitId);
//        visitFullDTO.drugs = listDrugFull(visitId);
//        visitFullDTO.conducts = listConducts(visitId).stream()
//                .map(this::extendConduct).collect(Collectors.toList());
//        return visitFullDTO;
    }

    // Text ////////////////////////////////////////////////////////////////////////////

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

    public DrugDTO getDrug(int drugId){
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
        String sql = xlate(
                "select d.*, m.* from Drug d, IyakuhinMaster m, Visit v " +
                        " where d.visitId = ? and d.visitId = v.visitId and d.iyakuhincode = m.iyakuhincode " +
                        " and " + ts.dialect.isValidAt("m.validFromz", "m.validUpto", "v.visitedAt") +
                        " order by d.drugId",
                ts.drugTable, "d", ts.iyakuhinMasterTable, "m", ts.visitTable, "v");
        return getQuery().query(sql, biProjector(ts.drugTable, ts.iyakuhinMasterTable, DrugFullDTO::create),
                visitId);
    }

    public List<DrugFullDTO> searchPrevDrug(String text, int patientId){
        throw new RuntimeException("not implemented");
    }

    // Shinryou ////////////////////////////////////////////////////////////////////////////

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

    public List<ShinryouFullDTO> listShinryouFull(int visitId){
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


    // Conduct ///////////////////////////////////////////////////////////////////////////////

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

    public void enterDisease(DiseaseDTO disease){
        throw new RuntimeException("not implemented");
    }

    public void modifyDisease(DiseaseDTO disease){
        throw new RuntimeException("not implemented");
    }

    public void deleteDisease(int diseaseId){
        throw new RuntimeException("not implemented");
    }

    public DiseaseFullDTO getDiseaseFull(int diseaseId){
        throw new RuntimeException("not implemented");
    }

    public List<DiseaseFullDTO> listCurrentDiseaseFull(int patientId){
        throw new RuntimeException("not implemented");
    }

    public List<DiseaseFullDTO> listDiseaseFull(int patientId){
        throw new RuntimeException("not implemented");
    }

    public void batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications){
        throw new RuntimeException("not implemented");
    }

    public List<DiseaseExampleDTO> listDiseaseExample(){
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

    public void enterPrescExample(PrescExampleDTO prescExample){
        throw new RuntimeException("not implemented");
    }

    public List<PrescExampleFullDTO> searchPrescExample(String text){
        throw new RuntimeException("not implemented");
    }

    public List<PrescExampleFullDTO> listAllPrescExample(){
        throw new RuntimeException("not implemented");
    }

    // PharmaQueue ///////////////////////////////////////////////////////////////////////

    public PharmaQueueDTO getPharmaQueue(int visitId){
        return ts.pharmaQueueTable.getById(visitId);
    }

    public void deletePharmaQueue(int visitId){
        PharmaQueueDTO pharmaQueue = getPharmaQueue(visitId);
        if( pharmaQueue == null ){
            return;
        }
        practiceLogger.logPharmaQueueDeleted(pharmaQueue);
    }

    // PracticeLog ///////////////////////////////////////////////////////////////////////

    private void enterPracticeLog(PracticeLogDTO practiceLog){
        ts.practiceLogTable.insert(practiceLog);
    }

    public PracticeLogDTO getLastPracticeLog(){
        String sql = xlate("select * from PracticeLog order by serialId desc limit 1",
                ts.practiceLogTable);
        return getQuery().get(sql, ts.practiceLogTable);
    }

    public int getLastPracticeLogId(){
        PracticeLogDTO plog = getLastPracticeLog();
        return plog == null ? 0 : plog.serialId;
    }

    public List<PracticeLogDTO> listPracticeLogSince(int afterThisId){
        String sql = xlate("select * from PracticeLog where serialId > ? ",
                ts.practiceLogTable);
        return getQuery().query(sql, ts.practiceLogTable, afterThisId);
    }

}
