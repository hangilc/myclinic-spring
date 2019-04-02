package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.consts.PharmaQueueState;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.backenddb.Query.Projector;
import static jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

public class Backend {

    private TableSet ts;
    private Query query;
    private SqlTranslator sqlTranslator = new SqlTranslator();
    private PracticeLogger practiceLogger;
    private HotlineLogger hotlineLogger;

    public Backend(TableSet ts, Query query) {
        this.ts = ts;
        this.query = query;
        this.practiceLogger = new PracticeLogger();
        practiceLogger.setSaver(this::enterPracticeLog);
        this.hotlineLogger = new HotlineLogger();
    }

    private static <S, T, U> Projector<U> biProjector(Projector<S> p1, Projector<T> p2, BiFunction<S, T, U> f) {
        return (rs, ctx) -> {
            S s = p1.project(rs, ctx);
            T t = p2.project(rs, ctx);
            return f.apply(s, t);
        };
    }

    public void setPracticeLogPublisher(Consumer<String> publisher) {
        practiceLogger.setPublisher(publisher::accept);
    }

    public void setHotlineLogPublisher(Consumer<String> publisher) {
        hotlineLogger.setHotlineLogPublisher(publisher::accept);
    }

    public Query getQuery() {
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

    public String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                        TableInfo tableInfo2, String alias2, TableInfo tableInfo3, String alias3,
                        TableInfo tableInfo4, String alias4) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2,
                tableInfo3, alias3, tableInfo4, alias4);
    }

    private int numberOfPages(int totalItems, int itemsPerPage) {
        if (totalItems == 0) {
            return 0;
        }
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }

    public void enterPatient(PatientDTO patient) {
        ts.patientTable.insert(patient);
        practiceLogger.logPatientCreated(patient);
    }

    public PatientDTO getPatient(int patientId) {
        return ts.patientTable.getById(patientId);
    }

    public void updatePatient(PatientDTO patient) {
        PatientDTO prev = getPatient(patient.patientId);
        ts.patientTable.update(patient);
        practiceLogger.logPatientUpdated(prev, patient);
    }

    public List<PatientDTO> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword) {
        String sql = xlate("select * from Patient where " +
                        " (lastName like ? or lastNameYomi like ?) and " +
                        " (firstName like ? or firstNameYomi like ?) ",
                ts.patientTable);
        return getQuery().query(sql, ts.patientTable,
                lastNameKeyword, lastNameKeyword, firstNameKeyword, firstNameKeyword);
    }

    public List<PatientDTO> searchPatientByKeyword(String keyword) {
        String sql = xlate("select * from Patient where " +
                        " (lastName like ? or lastNameYomi like ?) or " +
                        " (firstName like ? or firstNameYomi like ?) ",
                ts.patientTable);
        return getQuery().query(sql, ts.patientTable,
                keyword, keyword, keyword, keyword);
    }

    public List<PatientDTO> searchPatient(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        String[] parts = text.split("\\s+", 2);
        if (parts.length == 1) {
            String s = "%" + text + "%";
            return searchPatientByKeyword(s);
        } else {
            String last = "%" + parts[0] + "%";
            String first = "%" + parts[1] + "%";
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

    private void enterVisit(VisitDTO visit) {
        ts.visitTable.insert(visit);
        practiceLogger.logVisitCreated(visit);
    }

    public VisitDTO startVisit(int patientId, LocalDateTime at) {
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

    public void startExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.InExam.getCode());
    }

    public void suspendExam(int visitId) {
        changeWqueueState(visitId, WqueueWaitState.WaitReExam.getCode());
    }

    private boolean isTodaysVisit(VisitDTO visit) {
        return visit.visitedAt.substring(0, 10).equals(LocalDate.now().toString());
    }

    public void endExam(int visitId, int charge) {
        VisitDTO visit = ts.visitTable.getById(visitId);
        if (visit == null) {
            throw new RuntimeException("No such visit: " + visitId);
        }
        boolean isToday = isTodaysVisit(visit);
        setChargeOfVisit(visitId, charge);
        WqueueDTO wqueue = ts.wqueueTable.getById(visitId);
        if (wqueue != null && isToday) {
            changeWqueueState(visitId, WqueueWaitState.WaitCashier.getCode());
        } else {
            if (wqueue != null) { // it not today
                deleteWqueue(visitId);
            }
            WqueueDTO newWqueue = new WqueueDTO();
            newWqueue.visitId = visitId;
            newWqueue.waitState = WqueueWaitState.WaitCashier.getCode();
            enterWqueue(newWqueue);
        }
        PharmaQueueDTO pharmaQueue = getPharmaQueue(visitId);
        if (pharmaQueue != null) {
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

    public void enterCharge(ChargeDTO charge) {
        ts.chargeTable.insert(charge);
        practiceLogger.logChargeCreated(charge);
    }

    public void setChargeOfVisit(int visitId, int charge) {
        ChargeDTO prev = ts.chargeTable.getById(visitId);
        if (prev != null) {
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

    public ChargeDTO getCharge(int visitId) {
        return ts.chargeTable.getById(visitId);
    }

    // Payment ////////////////////////////////////////////////////////////////////////////

    public List<PaymentDTO> listPayment(int visitId) {
        String sql = xlate("select * from Payment where visit_id = ? order by paytime",
                ts.paymentTable);
        return getQuery().query(sql, ts.paymentTable, visitId);
    }

    // Wqueue /////////////////////////////////////////////////////////////////////////////

    private void enterWqueue(WqueueDTO wqueue) {
        ts.wqueueTable.insert(wqueue);
        practiceLogger.logWqueueCreated(wqueue);
    }

    public WqueueDTO getWqueue(int visitId) {
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
        if (wqueue == null) {
            return;
        }
        ts.wqueueTable.delete(visitId);
        practiceLogger.logWqueueDeleted(wqueue);
    }

    public List<WqueueDTO> listWqueue() {
        String sql = xlate("select * from Wqueue order by visitId", ts.wqueueTable);
        return getQuery().query(sql, ts.wqueueTable);
    }

    private WqueueFullDTO composeWqueueFullDTO(WqueueDTO wqueue) {
        WqueueFullDTO wqueueFullDTO = new WqueueFullDTO();
        wqueueFullDTO.wqueue = wqueue;
        wqueueFullDTO.visit = getVisit(wqueue.visitId);
        wqueueFullDTO.patient = getPatient(wqueueFullDTO.visit.patientId);
        return wqueueFullDTO;
    }

    public List<WqueueFullDTO> listWqueueFull() {
        return listWqueue().stream().map(this::composeWqueueFullDTO).collect(toList());
    }


    // Hoken //////////////////////////////////////////////////////////////////////////////

    public HokenDTO getHoken(int visitId) {
        VisitDTO visit = getVisit(visitId);
        return getHoken(visit);
    }

    public HokenDTO getHoken(VisitDTO visit) {
        HokenDTO hokenDTO = new HokenDTO();
        if (visit.shahokokuhoId > 0) {
            hokenDTO.shahokokuho = getShahokokuho(visit.shahokokuhoId);
        }
        if (visit.koukikoureiId > 0) {
            hokenDTO.koukikourei = getKoukikourei(visit.koukikoureiId);
        }
        if (visit.roujinId > 0) {
            hokenDTO.roujin = getRoujin(visit.roujinId);
        }
        if (visit.kouhi1Id > 0) {
            hokenDTO.kouhi1 = getKouhi(visit.kouhi1Id);
        }
        if (visit.kouhi2Id > 0) {
            hokenDTO.kouhi2 = getKouhi(visit.kouhi2Id);
        }
        if (visit.kouhi3Id > 0) {
            hokenDTO.kouhi3 = getKouhi(visit.kouhi3Id);
        }
        return hokenDTO;
    }

    public HokenDTO listAvailableHoken(int patientId, LocalDate visitedAt) {
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

    public void updateHoken(VisitDTO visit) {
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

    public DrugAttrDTO getDrugAttr(int drugId) {
        return ts.drugAttrTable.getById(drugId);
    }

    public void enterDrugAttr(DrugAttrDTO drugAttr) {
        ts.drugAttrTable.insert(drugAttr);
    }

    private void updateDrugAttr(DrugAttrDTO drugAttr) {
        ts.drugAttrTable.update(drugAttr);
    }

    private void deleteDrugAttr(int drugId) {
        ts.drugAttrTable.delete(drugId);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugIds.stream().map(ts.drugAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public DrugAttrDTO setDrugTekiyou(int drugId, String tekiyou) {
        DrugAttrDTO attr = ts.drugAttrTable.getById(drugId);
        if (attr != null) {
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

    public void deleteDrugTekiyou(int drugId) {
        DrugAttrDTO attr = ts.drugAttrTable.getById(drugId);
        if (attr == null) {
            return;
        }
        attr.tekiyou = null;
        if (DrugAttrDTO.isEmpty(attr)) {
            deleteDrugAttr(drugId);
        } else {
            updateDrugAttr(attr);
        }
    }

    public int countUnprescribedDrug(int visitId) {
        String sql = xlate("select count(*) from Drug where visitId = ? and prescribed = 0",
                ts.drugTable);
        return getQuery().get(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), visitId);
    }

    // Visit ////////////////////////////////////////////////////////////////////////////

    public VisitDTO getVisit(int visitId) {
        return ts.visitTable.getById(visitId);
    }

    private void updateVisit(VisitDTO visit) {
        VisitDTO prev = getVisit(visit.visitId);
        ts.visitTable.update(visit);
        practiceLogger.logVisitUpdated(prev, visit);
    }

    public void deleteVisitSafely(int visitId) {
        VisitFullDTO visit = getVisitFull(visitId);
        if (visit.texts.size() > 0) {
            throw new CannotDeleteVisitSafelyException("文章があるので、診察を削除できません。");
        }
        if (visit.drugs.size() > 0) {
            throw new CannotDeleteVisitSafelyException("投薬があるので、診察を削除できません。");
        }
        if (visit.shinryouList.size() > 0) {
            throw new CannotDeleteVisitSafelyException("診療行為があるので、診察を削除できません。");
        }
        if (visit.conducts.size() > 0) {
            throw new CannotDeleteVisitSafelyException("処置があるので、診察を削除できません。");
        }
        ChargeDTO charge = getCharge(visitId);
        if (charge != null) {
            throw new CannotDeleteVisitSafelyException("請求があるので、診察を削除できません。");
        }
        List<PaymentDTO> payments = listPayment(visitId);
        if (payments.size() > 0) {
            throw new CannotDeleteVisitSafelyException("支払い記録があるので、診察を削除できません。");
        }
        WqueueDTO wqueue = getWqueue(visitId);
        if (wqueue != null) {
            deleteWqueue(visitId);
        }
        PharmaQueueDTO pharmaQueue = getPharmaQueue(visitId);
        if (pharmaQueue != null) {
            deletePharmaQueue(visitId);
        }
        deleteVisit(visitId);
    }

    private void deleteVisit(int visitId) {
        VisitDTO visit = ts.visitTable.getById(visitId);
        ts.visitTable.delete(visitId);
        practiceLogger.logVisitDeleted(visit);
    }

    public List<VisitPatientDTO> listRecentVisitWithPatient(int page, int itemsPerPage) {
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

    public List<VisitPatientDTO> listTodaysVisit() {
        String sql = xlate("select v.*, p.* from Visit v, Patient p where date(v.visitedAt) = ? " +
                        " and v.patientId = p.patientId order by v.visitId",
                ts.visitTable, "v", ts.patientTable, "p");
        return getQuery().query(sql,
                biProjector(ts.visitTable, ts.patientTable, VisitPatientDTO::create),
                LocalDate.now().toString());
    }

    private int countVisitByPatient(int patientId) {
        String sql = xlate("select count(*) from Visit where patientId = ?",
                ts.patientTable);
        return getQuery().get(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), patientId);
    }

    public VisitFull2PageDTO listVisitFull2(int patientId, int page) {
        int itemsPerPage = 10;
        int nVisit = countVisitByPatient(patientId);
        List<VisitDTO> visits = Collections.emptyList();
        if (nVisit > 0) {
            String sql = xlate("select * from Visit where patientId = ? " +
                            " order by visitId desc limit ? offset ?",
                    ts.visitTable);
            visits = getQuery().query(sql, ts.visitTable, patientId,
                    itemsPerPage, itemsPerPage * page);
        }
        VisitFull2PageDTO visitFull2PageDTO = new VisitFull2PageDTO();
        visitFull2PageDTO.totalPages = numberOfPages(nVisit, itemsPerPage);
        visitFull2PageDTO.page = page;
        visitFull2PageDTO.visits = visits.stream().map(this::getVisitFull2).collect(toList());
        return visitFull2PageDTO;
    }

    public VisitFullDTO getVisitFull(int visitId) {
        VisitDTO visit = getVisit(visitId);
        System.out.println("visit:" + visit);
        return getVisitFull(visit);
    }

    private VisitFullDTO getVisitFull(VisitDTO visitDTO) {
        int visitId = visitDTO.visitId;
        VisitFullDTO visitFullDTO = new VisitFullDTO();
        visitFullDTO.visit = visitDTO;
        visitFullDTO.texts = listText(visitId);
        visitFullDTO.shinryouList = listShinryouFull(visitId);
        visitFullDTO.drugs = listDrugFull(visitId);
        visitFullDTO.conducts = listConductFull(visitId);
        return visitFullDTO;
    }

    private VisitFull2DTO getVisitFull2(VisitDTO visit) {
        int visitId = visit.visitId;
        VisitFull2DTO visitFull2DTO = new VisitFull2DTO();
        visitFull2DTO.visit = visit;
        visitFull2DTO.texts = listText(visitId);
        visitFull2DTO.shinryouList = listShinryouFull(visitId);
        visitFull2DTO.drugs = listDrugFull(visitId);
        visitFull2DTO.conducts = listConductFull(visitId);
        visitFull2DTO.hoken = getHoken(visit);
        visitFull2DTO.charge = getCharge(visitId);
        return visitFull2DTO;
    }

    // Shouki //////////////////////////////////////////////////////////////////////////

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitIds.stream().map(ts.shoukiTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public void updateShouki(ShoukiDTO shouki) {
        ts.shoukiTable.update(shouki);
    }

    public void deleteShouki(int visitId) {
        ts.shoukiTable.delete(visitId);
    }

    // Text ////////////////////////////////////////////////////////////////////////////

    public void enterText(TextDTO text) {
        ts.textTable.insert(text);
        practiceLogger.logTextCreated(text);
    }

    public TextDTO getText(int textId) {
        return ts.textTable.getById(textId);
    }

    public void updateText(TextDTO text) {
        TextDTO prev = getText(text.textId);
        ts.textTable.update(text);
        practiceLogger.logTextUpdated(prev, text);
    }

    public void deleteText(int textId) {
        TextDTO text = getText(textId);
        ts.textTable.delete(textId);
        practiceLogger.logTextDeleted(text);
    }

    public List<TextDTO> listText(int visitId) {
        String sql = xlate("select * from Text where visitId = ? order by textId",
                ts.textTable);
        return getQuery().query(sql, ts.textTable, visitId);
    }

    public TextVisitPageDTO searchText(int patientId, String text, int page) {
        int itemsPerPage = 20;
        String searchText = "%" + text + "%";
        String countSql = xlate("select count(*) from Text t, Visit v " +
                        " where t.visitId = v.visitId and v.patientId = ? " +
                        " and t.content like ? ",
                ts.textTable, "t", ts.visitTable, "v");
        int totalItems = getQuery().getInt(countSql, patientId, searchText);
        String sql = xlate("select t.*, v.* from Text t, Visit v " +
                        " where t.visitId = v.visitId and v.patientId = ? " +
                        " and t.content like ? order by t.textId limit ? offset ?",
                ts.textTable, "t", ts.visitTable, "v");
        List<TextVisitDTO> textVisits = getQuery().query(sql,
                biProjector(ts.textTable, ts.visitTable, TextVisitDTO::create),
                patientId, searchText, itemsPerPage, itemsPerPage * page);
        TextVisitPageDTO result = new TextVisitPageDTO();
        result.page = page;
        result.totalPages = numberOfPages(totalItems, itemsPerPage);
        result.textVisits = textVisits;
        return result;
    }

    public TextVisitPatientPageDTO searchTextGlobally(String text, int page) {
        int itemsPerPage = 20;
        String searchText = "%" + text + "%";
        String countSql = xlate("select count(*) from Text where content like ?", ts.textTable);
        int totalItems = getQuery().getInt(countSql, searchText);
        String sql = xlate("select t.*, v.*, p.* from Text t, Visit v, Patient p " +
                        " where t.visitId = v.visitId and v.patientId = p.patientId " +
                        " and t.content like ? order by t.textId limit ? offset ?",
                ts.textTable, "t", ts.visitTable, "v", ts.patientTable, "p");
        List<TextVisitPatientDTO> textVisits = getQuery().query(sql,
                (rs, ctx) -> {
                    TextVisitPatientDTO row = new TextVisitPatientDTO();
                    row.text = ts.textTable.project(rs, ctx);
                    row.visit = ts.visitTable.project(rs, ctx);
                    row.patient = ts.patientTable.project(rs, ctx);
                    return row;
                },
                searchText, itemsPerPage, itemsPerPage * page);
        TextVisitPatientPageDTO result = new TextVisitPatientPageDTO();
        result.page = page;
        result.totalPages = numberOfPages(totalItems, itemsPerPage);
        result.textVisitPatients = textVisits;
        return result;
    }

    public DrugDTO getDrug(int drugId) {
        return ts.drugTable.getById(drugId);
    }

    public void enterDrug(DrugDTO drug) {
        ts.drugTable.insert(drug);
        practiceLogger.logDrugCreated(drug);
    }

    public void updateDrug(DrugDTO drug) {
        DrugDTO prev = getDrug(drug.drugId);
        ts.drugTable.update(drug);
        practiceLogger.logDrugUpdated(prev, drug);
    }

    public void batchUpdateDrugDays(List<Integer> drugIds, int days) {
        drugIds.forEach(drugId -> {
            DrugDTO drug = getDrug(drugId);
            drug.days = days;
            updateDrug(drug);
        });
    }

    public void deleteDrug(int drugId) {
        DrugDTO drug = ts.drugTable.getById(drugId);
        ts.drugTable.delete(drugId);
        practiceLogger.logDrugDeleted(drug);
    }

    public void batchDeleteDrugs(List<Integer> drugIds) {
        drugIds.forEach(this::deleteDrug);
    }

    public DrugFullDTO getDrugFull(int drugId) {
        String sql = xlate("select d.*, m.* from Drug d, IyakuhinMaster m, Visit v " +
                        " where d.drugId = ? and d.visitId = v.visitId and d.iyakuhincode = m.iyakuhincode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.drugTable, "d", ts.iyakuhinMasterTable, "m", ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.drugTable, ts.iyakuhinMasterTable, DrugFullDTO::create),
                drugId);
    }

    public List<DrugFullDTO> listDrugFull(int visitId) {
        String sql = xlate(
                "select d.*, m.* from Drug d, IyakuhinMaster m, Visit v " +
                        " where d.visitId = ? and d.visitId = v.visitId and d.iyakuhincode = m.iyakuhincode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by d.drugId",
                ts.drugTable, "d", ts.iyakuhinMasterTable, "m", ts.visitTable, "v");
        return getQuery().query(sql, biProjector(ts.drugTable, ts.iyakuhinMasterTable, DrugFullDTO::create),
                visitId);
    }

    private List<Integer> listRepresentativeNaifukuTonpukuDrugId(int patientId) {
        String sql = xlate("select MAX(d.drugId) from Drug d, Visit v where d.visitId = v.visitId " +
                        " and v.patientId = ? " +
                        " and d.category in (0, 1) " +
                        " group by d.iyakuhincode, d.amount, d.usage, d.days ",
                ts.drugTable, "d", ts.visitTable, "v");
        return getQuery().query(sql,
                (rs, ctx) -> rs.getInt(ctx.nextIndex()),
                patientId);
    }

    private List<Integer> listRepresentativeNaifukuTonpukuDrugId(String text, int patientId) {
        String searchText = "%" + text + "%";
        String sql = xlate("select MAX(d.drugId) from Drug d, Visit v, IyakuhinMaster m " +
                        " where d.visitId = v.visitId " +
                        " and v.patientId = ? " +
                        " and d.category in (0, 1) " +
                        " and d.iyakuhincode = m.iyakuhincode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " and m.name like ? " +
                        " group by d.iyakuhincode, d.amount, d.usage, d.days ",
                ts.drugTable, "d", ts.visitTable, "v", ts.iyakuhinMasterTable, "m");
        return getQuery().query(sql,
                (rs, ctx) -> rs.getInt(ctx.nextIndex()),
                patientId, searchText);
    }

    private List<Integer> listRepresentativeGaiyouDrugId(int patientId) {
        String sql = xlate("select MAX(d.drugId) from Drug d, Visit v where d.visitId = v.visitId " +
                        " and v.patientId = ? " +
                        " and d.category = 2 " +
                        " group by d.iyakuhincode, d.amount, d.usage ",
                ts.drugTable, "d", ts.visitTable, "v");
        return getQuery().query(sql,
                (rs, ctx) -> rs.getInt(ctx.nextIndex()),
                patientId);
    }

    public List<Integer> listRepresentativeGaiyouDrugId(String text, int patientId) {
        String searchText = "%" + text + "%";
        String sql = xlate("select MAX(d.drugId) from Drug d, Visit v, IyakuhinMaster m " +
                        " where d.visitId = v.visitId " +
                        " and v.patientId = ? " +
                        " and d.category = 2 " +
                        " and d.iyakuhincode = m.iyakuhincode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " and m.name like ? " +
                        " group by d.iyakuhincode, d.amount, d.usage ",
                ts.drugTable, "d", ts.visitTable, "v", ts.iyakuhinMasterTable, "m");
        return getQuery().query(sql,
                (rs, ctx) -> rs.getInt(ctx.nextIndex()),
                patientId, searchText);
    }

    public List<DrugFullDTO> searchPrevDrug(int patientId) {
        return Stream.concat(
                listRepresentativeNaifukuTonpukuDrugId(patientId).stream(),
                listRepresentativeGaiyouDrugId(patientId).stream()
        ).sorted(Comparator.<Integer>naturalOrder().reversed())
                .map(this::getDrugFull)
                .collect(toList());
    }

    public List<DrugFullDTO> searchPrevDrug(String text, int patientId) {
        return Stream.concat(
                listRepresentativeNaifukuTonpukuDrugId(text, patientId).stream(),
                listRepresentativeGaiyouDrugId(text, patientId).stream()
        ).sorted(Comparator.<Integer>naturalOrder().reversed())
                .map(this::getDrugFull)
                .collect(toList());
    }

    // Shinryou ////////////////////////////////////////////////////////////////////////////

    public ShinryouDTO getShinryou(int shinryouId) {
        return ts.shinryouTable.getById(shinryouId);
    }

    public void enterShinryou(ShinryouDTO shinryou) {
        ts.shinryouTable.insert(shinryou);
        practiceLogger.logShinryouCreated(shinryou);
    }

    public void deleteShinryou(int shinryouId) {
        ShinryouDTO shinryou = getShinryou(shinryouId);
        ts.shinryouTable.delete(shinryouId);
        practiceLogger.logShinryouDeleted(shinryou);
    }

    public ShinryouFullDTO getShinryouFull(int shinryouId) {
        String sql = xlate("select s.*, m.* from Shinryou s, ShinryouMaster m, Visit v " +
                        " where s.visitId = v.visitId and s.shinryoucode = m.shinryoucode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.shinryouTable, "s", ts.shinryouMasterTable, "m", ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.shinryouTable, ts.shinryouMasterTable, ShinryouFullDTO::create));
    }

    public void batchEnterShinryou(List<ShinryouDTO> shinryouList) {
        shinryouList.forEach(this::enterShinryou);
    }

    public BatchEnterResultDTO batchEnter(BatchEnterRequestDTO req) {
        BatchEnterResultDTO result = new BatchEnterResultDTO();
        result.shinryouIds = new ArrayList<>();
        result.conductIds = new ArrayList<>();
        if (req.shinryouList != null) {
            req.shinryouList.forEach(shinryou -> {
                enterShinryou(shinryou);
                result.shinryouIds.add(shinryou.shinryouId);
            });
        }
        if (req.conducts != null) {
            req.conducts.forEach(conductReq -> {
                ConductFullDTO c = enterConductFull(conductReq);
                result.conductIds.add(c.conduct.conductId);
            });
        }
        return result;
    }

    public List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(this::getShinryouFull).collect(toList());
    }

    public List<ShinryouFullDTO> listShinryouFull(int visitId) {
        String sql = xlate("select s.*, m.* from Shinryou s, ShinryouMaster m, Visit v " +
                        " where s.visitId = ? and s.visitId = v.visitId and s.shinryoucode = m.shinryoucode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by s.shinryouId",
                ts.shinryouTable, "s", ts.shinryouMasterTable, "m", ts.visitTable, "v");
        return getQuery().query(sql,
                biProjector(ts.shinryouTable, ts.shinryouMasterTable, ShinryouFullDTO::create),
                visitId);
    }

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(ts.shinryouAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public ShinryouAttrDTO getShinryouAttr(int shinryouId) {
        return ts.shinryouAttrTable.getById(shinryouId);
    }

    public void enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        ts.shinryouAttrTable.insert(shinryouAttr);
    }


    // Conduct ///////////////////////////////////////////////////////////////////////////////

    public void enterConduct(ConductDTO conduct) {
        ts.conductTable.insert(conduct);
        practiceLogger.logConductCreated(conduct);
    }

    public ConductFullDTO enterConductFull(ConductEnterRequestDTO req) {
        ConductFullDTO result = new ConductFullDTO();
        ConductDTO conduct = new ConductDTO();
        conduct.visitId = req.visitId;
        conduct.kind = req.kind;
        enterConduct(conduct);
        result.conduct = conduct;
        int conductId = conduct.conductId;
        if (req.gazouLabel != null) {
            GazouLabelDTO gazouLabel = new GazouLabelDTO();
            gazouLabel.conductId = conductId;
            gazouLabel.label = req.gazouLabel;
            enterGazouLabel(gazouLabel);
            result.gazouLabel = gazouLabel;
        }
        if (req.shinryouList != null) {
            result.conductShinryouList = new ArrayList<>();
            req.shinryouList.forEach(shinryou -> {
                shinryou.conductId = conductId;
                enterConductShinryou(shinryou);
                result.conductShinryouList.add(getConductShinryouFull(shinryou.conductShinryouId));
            });
        }
        if (req.drugs != null) {
            result.conductDrugs = new ArrayList<>();
            req.drugs.forEach(drug -> {
                drug.conductId = conductId;
                enterConductDrug(drug);
                result.conductDrugs.add(getConductDrugFull(drug.conductDrugId));
            });
        }
        if (req.kizaiList != null) {
            result.conductKizaiList = new ArrayList<>();
            req.kizaiList.forEach(kizai -> {
                kizai.conductId = conductId;
                enterConductKizai(kizai);
                result.conductKizaiList.add(getConductKizaiFull(kizai.conductKizaiId));
            });
        }
        return result;
    }

    public ConductDTO getConduct(int conductId) {
        return ts.conductTable.getById(conductId);
    }

    private void deleteConduct(int conductId) {
        ConductDTO conduct = getConduct(conductId);
        ts.conductTable.delete(conductId);
        practiceLogger.logConductDeleted(conduct);
    }

    public void deleteConductCascading(int conductId) {
        GazouLabelDTO gazouLabel = getGazouLabel(conductId);
        if (gazouLabel != null) {
            deleteGazouLabel(conductId);
        }
        listConductShinryou(conductId).forEach(s -> deleteConductShinryou(s.conductShinryouId));
        listConductDrug(conductId).forEach(s -> deleteConductDrug(s.conductDrugId));
        listConductKizai(conductId).forEach(s -> deleteConductKizai(s.conductKizaiId));
        deleteConduct(conductId);
    }

    public void modifyConductKind(int conductId, int conductKind) {
        ConductDTO prev = getConduct(conductId);
        ConductDTO updated = ConductDTO.copy(prev);
        updated.kind = conductKind;
        ts.conductTable.update(updated);
        practiceLogger.logConductUpdated(prev, updated);
    }

    public List<ConductDTO> listConduct(int visitId) {
        String sql = xlate("select * from Conduct where visitId = ?", ts.conductTable);
        return getQuery().query(sql, ts.conductTable, visitId);
    }

    public List<ConductFullDTO> listConductFullByIds(List<Integer> conductIds) {
        return conductIds.stream().map(this::getConductFull).collect(toList());
    }

    private ConductFullDTO extendConduct(ConductDTO conduct) {
        int conductId = conduct.conductId;
        ConductFullDTO conductFullDTO = new ConductFullDTO();
        conductFullDTO.conduct = conduct;
        conductFullDTO.gazouLabel = ts.gazouLabelTable.getById(conductId);
        conductFullDTO.conductShinryouList = listConductShinryouFull(conductId);
        conductFullDTO.conductDrugs = listConductDrugFull(conductId);
        conductFullDTO.conductKizaiList = listConductKizaiFull(conductId);
        return conductFullDTO;
    }

    public ConductFullDTO getConductFull(int conductId) {
        ConductDTO conduct = ts.conductTable.getById(conductId);
        return extendConduct(conduct);
    }

    public List<ConductFullDTO> listConductFull(int visitId) {
        return listConduct(visitId).stream().map(this::extendConduct).collect(toList());
    }

    // GazouLabel ///////////////////////////////////////////////////////////////////////////

    public void enterGazouLabel(GazouLabelDTO gazouLabel) {
        ts.gazouLabelTable.insert(gazouLabel);
        practiceLogger.logGazouLabelCreated(gazouLabel);
    }

    public GazouLabelDTO getGazouLabel(int conductId) {
        return ts.gazouLabelTable.getById(conductId);
    }

    public void deleteGazouLabel(int conductId) {
        GazouLabelDTO deleted = getGazouLabel(conductId);
        ts.gazouLabelTable.delete(conductId);
        practiceLogger.logGazouLabelDeleted(deleted);
    }

    // ConductShinryou //////////////////////////////////////////////////////////////////////

    public void enterConductShinryou(ConductShinryouDTO shinryou) {
        ts.conductShinryouTable.insert(shinryou);
        practiceLogger.logConductShinryouCreated(shinryou);
    }

    public ConductShinryouDTO getConductShinryou(int conductShinryouId) {
        return ts.conductShinryouTable.getById(conductShinryouId);
    }

    public void deleteConductShinryou(int conductShinryouId) {
        ConductShinryouDTO deleted = getConductShinryou(conductShinryouId);
        ts.conductShinryouTable.delete(conductShinryouId);
        practiceLogger.logConductShinryouDeleted(deleted);
    }

    public List<ConductShinryouDTO> listConductShinryou(int conductId) {
        String sql = xlate("select * from ConductShinryou where conductId = ? order by conductShinryouId",
                ts.conductShinryouTable);
        return getQuery().query(sql, ts.conductShinryouTable, conductId);
    }

    public ConductShinryouFullDTO getConductShinryouFull(int conductShinryouId) {
        String sql = xlate("select s.*, m.* from ConductShinryou s, Conduct c, ShinryouMaster m, Visit v " +
                        " where s.conductShinryouId = ? and s.conductId = c.conductId and c.visitId = v.visitId " +
                        " and s.shinryoucode = m.shinryoucode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.conductShinryouTable, "s", ts.conductTable, "c", ts.shinryouMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.conductShinryouTable, ts.shinryouMasterTable, ConductShinryouFullDTO::create),
                conductShinryouId);
    }

    public List<ConductShinryouFullDTO> listConductShinryouFull(int conductId) {
        String sql = xlate("select s.*, m.* from ConductShinryou s, Conduct c, ShinryouMaster m, Visit v " +
                        " where s.conductId = ? and s.conductId = c.conductId and c.visitId = v.visitId " +
                        " and s.shinryoucode = m.shinryoucode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by s.conductShinryouId",
                ts.conductShinryouTable, "s", ts.conductTable, "c", ts.shinryouMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().query(sql,
                biProjector(ts.conductShinryouTable, ts.shinryouMasterTable, ConductShinryouFullDTO::create),
                conductId);
    }

    // ConductDrug ///////////////////////////////////////////////////////////////////////////

    public void enterConductDrug(ConductDrugDTO drug) {
        ts.conductDrugTable.insert(drug);
        practiceLogger.logConductDrugCreated(drug);
    }

    public ConductDrugDTO getConductDrug(int conductDrugId) {
        return ts.conductDrugTable.getById(conductDrugId);
    }

    public void deleteConductDrug(int conductDrugId) {
        ConductDrugDTO deleted = getConductDrug(conductDrugId);
        ts.conductDrugTable.delete(conductDrugId);
        practiceLogger.logConductDrugDeleted(deleted);
    }

    public List<ConductDrugDTO> listConductDrug(int conductId) {
        String sql = xlate("select * from ConductDrug where conductId = ? order by conductDrugId",
                ts.conductDrugTable);
        return getQuery().query(sql, ts.conductDrugTable, conductId);
    }

    public ConductDrugFullDTO getConductDrugFull(int conductDrugId) {
        String sql = xlate("select d.*, m.* from ConductDrug d, Conduct c, IyakuhinMaster m, Visit v " +
                        " where d.conductDrugId = ? and d.conductId = c.conductId and c.visitId = v.visitId " +
                        " and d.iyakuhincode = m.iyakuhincode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.conductDrugTable, "d", ts.conductTable, "c", ts.iyakuhinMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.conductDrugTable, ts.iyakuhinMasterTable, ConductDrugFullDTO::create),
                conductDrugId);
    }

    public List<ConductDrugFullDTO> listConductDrugFull(int conductId) {
        String sql = xlate("select d.*, m.* from ConductDrug d, Conduct c, IyakuhinMaster m, Visit v " +
                        " where d.conductId = ? and d.conductId = c.conductId and c.visitId = v.visitId " +
                        " and d.iyakuhincode = m.iyakuhincode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by d.conductDrugId",
                ts.conductDrugTable, "d", ts.conductTable, "c", ts.iyakuhinMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().query(sql,
                biProjector(ts.conductDrugTable, ts.iyakuhinMasterTable, ConductDrugFullDTO::create),
                conductId);
    }

    // ConductKizai //////////////////////////////////////////////////////////////////////////

    public void enterConductKizai(ConductKizaiDTO kizai) {
        ts.conductKizaiTable.insert(kizai);
        practiceLogger.logConductKizaiCreated(kizai);
    }

    public ConductKizaiDTO getConductKizai(int conductKizaiId) {
        return ts.conductKizaiTable.getById(conductKizaiId);
    }

    public void deleteConductKizai(int conductKizaiId) {
        ConductKizaiDTO deleted = getConductKizai(conductKizaiId);
        ts.conductKizaiTable.delete(conductKizaiId);
        practiceLogger.logConductKizaiDeleted(deleted);
    }

    public List<ConductKizaiDTO> listConductKizai(int conductId) {
        String sql = xlate("select * from ConductKizai where conductId = ? order by conductKizaiId",
                ts.conductKizaiTable);
        return getQuery().query(sql, ts.conductKizaiTable, conductId);
    }

    public ConductKizaiFullDTO getConductKizaiFull(int conductKizaiId) {
        String sql = xlate("select k.*, m.* from ConductKizai k, Conduct c, KizaiMaster m, Visit v " +
                        " where k.conductKizaiId = ? and k.conductId = c.conductId and c.visitId = v.visitId " +
                        " and k.kizaicode = m.kizaicode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.conductKizaiTable, "k", ts.conductTable, "c", ts.kizaiMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.conductKizaiTable, ts.kizaiMasterTable, ConductKizaiFullDTO::create),
                conductKizaiId);
    }

    public List<ConductKizaiFullDTO> listConductKizaiFull(int conductId) {
        String sql = xlate("select k.*, m.* from ConductKizai k, Conduct c, KizaiMaster m, Visit v " +
                        " where k.conductId = ? and k.conductId = c.conductId and c.visitId = v.visitId " +
                        " and k.kizaicode = m.kizaicode and " +
                        ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by k.conductKizaiId",
                ts.conductKizaiTable, "k", ts.conductTable, "c", ts.kizaiMasterTable, "m",
                ts.visitTable, "v");
        return getQuery().query(sql,
                biProjector(ts.conductKizaiTable, ts.kizaiMasterTable, ConductKizaiFullDTO::create),
                conductId);
    }

    // Shahokokuho //////////////////////////////////////////////////////////////////////////////

    public ShahokokuhoDTO getShahokokuho(int shahokokuhoId) {
        return ts.shahokokuhoTable.getById(shahokokuhoId);
    }

    // Koukikourei //////////////////////////////////////////////////////////////////////////////

    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return ts.koukikoureiTable.getById(koukikoureiId);
    }

    // Roujin ////////////////////////////////////////////////////////////////////////////////////

    public RoujinDTO getRoujin(int roujinId) {
        return ts.roujinTable.getById(roujinId);
    }

    // Kouhi //////////////////////////////////////////////////////////////////////////////////////

    public KouhiDTO getKouhi(int kouhiId) {
        return ts.kouhiTable.getById(kouhiId);
    }

    // Disease ////////////////////////////////////////////////////////////////////////////////////

    public void enterDisease(DiseaseDTO disease) {
        ts.diseaseTable.insert(disease);
        practiceLogger.logDiseaseCreated(disease);
    }

    public DiseaseDTO getDisease(int diseaseId) {
        return ts.diseaseTable.getById(diseaseId);
    }

    public void updateDisease(DiseaseDTO disease) {
        DiseaseDTO prev = getDisease(disease.diseaseId);
        ts.diseaseTable.update(disease);
        practiceLogger.logDiseaseUpdated(prev, disease);
    }

    public void deleteDisease(int diseaseId) {
        DiseaseDTO deleted = getDisease(diseaseId);
        ts.diseaseTable.delete(diseaseId);
        practiceLogger.logDiseaseDeleted(deleted);
    }

    public DiseaseFullDTO getDiseaseFull(int diseaseId) {
        String diseaseSql = xlate("select d.*, m.* from Disease d, ByoumeiMaster m " +
                        " where d.diseaseId = ? and d.shoubyoumeicode = m.shoubyoumeicode " +
                        " and " + ts.dialect.isValidAt("m.valid_from", "m.validUpto", "d.startDate"),
                ts.diseaseTable, "d", ts.byoumeiMasterTable, "m");
        DiseaseFullDTO result = getQuery().get(diseaseSql,
                biProjector(ts.diseaseTable, ts.byoumeiMasterTable, DiseaseFullDTO::create),
                diseaseId);
        String adjSql = xlate("select a.*, m.* from DiseaseAdj a, ShuushokugoMaster m " +
                        " where a.diseaseId = ? and a.shuushokugocode = m.shuushokugocode ",
                ts.diseaseAdjTable, "a", ts.shuushokugoMasterTable, "m");
        result.adjList = getQuery().query(adjSql,
                biProjector(ts.diseaseAdjTable, ts.shuushokugoMasterTable, DiseaseAdjFullDTO::create),
                diseaseId);
        return result;
    }

    public List<DiseaseFullDTO> listCurrentDiseaseFull(int patientId) {
        String sql = xlate("select diseaseId from Disease where patientId = ? " +
                        " and " + ts.dialect.isValidUptoUnbound("endDate") +
                        " order by diseaseId ",
                ts.diseaseTable);
        List<Integer> diseaseIds = getQuery().query(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), patientId);
        return diseaseIds.stream().map(this::getDiseaseFull).collect(toList());
    }

    public List<DiseaseFullDTO> listDiseaseFull(int patientId) {
        String sql = xlate("select diseaseId from Disease where patientId = ? order by diseaseId ",
                ts.diseaseTable);
        List<Integer> diseaseIds = getQuery().query(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), patientId);
        return diseaseIds.stream().map(this::getDiseaseFull).collect(toList());
    }

    public void batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications) {
        for (DiseaseModifyEndReasonDTO modify : modifications) {
            DiseaseDTO d = getDisease(modify.diseaseId);
            d.endDate = modify.endDate;
            d.endReason = modify.endReason;
            updateDisease(d);
        }
    }

    // PharmaQueue ///////////////////////////////////////////////////////////////////////

    public PharmaQueueDTO getPharmaQueue(int visitId) {
        return ts.pharmaQueueTable.getById(visitId);
    }

    public void deletePharmaQueue(int visitId) {
        PharmaQueueDTO pharmaQueue = getPharmaQueue(visitId);
        if (pharmaQueue == null) {
            return;
        }
        practiceLogger.logPharmaQueueDeleted(pharmaQueue);
    }

    // ShinryouMaster ////////////////////////////////////////////////////////////////////

    public ShinryouMasterDTO findShinryouMasterByName(String name, LocalDate at) {
        String sql = xlate("select * from ShinryouMaster where name = ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?") +
                        " limit 1",
                ts.shinryouMasterTable);
        String atString = at.toString();
        List<ShinryouMasterDTO> matches = getQuery().query(sql,
                ts.shinryouMasterTable, name, atString, atString);
        return matches.size() == 0 ? null : matches.get(0);
    }

    public ShinryouMasterDTO resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at) {
        for (String name : nameCandidates) {
            ShinryouMasterDTO m = findShinryouMasterByName(name, at);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    public Map<String, Integer> batchResolveShinryouNames(List<List<String>> args, LocalDate at) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                ShinryouMasterDTO m = findShinryouMasterByName(key, at);
                if (m != null) {
                    result.put(key, m.shinryoucode);
                }
            } else {
                ShinryouMasterDTO m = resolveShinryouMasterByName(arg.subList(1, arg.size()), at);
                if (m != null) {
                    result.put(key, m.shinryoucode);
                }
            }
        }
        return result;
    }

    public List<ShinryouMasterDTO> searchShinryouMaster(String text, LocalDate at) {
        String sql = xlate("select * from ShinryouMaster where name like ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.shinryouMasterTable);
        String searchText = "%" + text + "%";
        String atString = at.toString();
        return getQuery().query(sql, ts.shinryouMasterTable, searchText, atString, atString);
    }

    // IyakuhinMaster /////////////////////////////////////////////////////////////////////

    public List<IyakuhinMasterDTO> searchIyakuhinMaster(String text, LocalDate at) {
        String sql = xlate("select * from IyakuhinMaster where name like ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.iyakuhinMasterTable);
        String searchText = "%" + text + "%";
        String atString = at.toString();
        return getQuery().query(sql, ts.iyakuhinMasterTable, searchText, atString, atString);
    }

    // KizaiMaster ///////////////////////////////////////////////////////////////////////

    public KizaiMasterDTO findKizaiMasterByName(String name, LocalDate at) {
        String sql = xlate("select * from KizaiMaster where name = ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?") +
                        " limit 1",
                ts.kizaiMasterTable);
        String atString = at.toString();
        List<KizaiMasterDTO> matches = getQuery().query(sql,
                ts.kizaiMasterTable, name, atString, atString);
        return matches.size() == 0 ? null : matches.get(0);
    }

    public KizaiMasterDTO resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at) {
        for (String name : nameCandidates) {
            KizaiMasterDTO m = findKizaiMasterByName(name, at);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    public Map<String, Integer> batchResolveKizaiNames(List<List<String>> args, LocalDate at) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (List<String> arg : args) {
            if (arg.size() < 1) {
                continue;
            }
            String key = arg.get(0);
            if (arg.size() == 1) {
                KizaiMasterDTO m = findKizaiMasterByName(key, at);
                if (m != null) {
                    result.put(key, m.kizaicode);
                }
            } else {
                KizaiMasterDTO m = resolveKizaiMasterByName(arg.subList(1, arg.size()), at);
                if (m != null) {
                    result.put(key, m.kizaicode);
                }
            }
        }
        return result;
    }

    public List<KizaiMasterDTO> searchKizaiMaster(String text, LocalDate at) {
        String sql = xlate("select * from KizaiMaster where name like ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.kizaiMasterTable);
        String searchText = "%" + text + "%";
        String atString = at.toString();
        return getQuery().query(sql, ts.kizaiMasterTable, searchText, atString, atString);
    }

    // ByoumeiMaster /////////////////////////////////////////////////////////////////////

    public List<ByoumeiMasterDTO> searchByoumeiMaster(String text, LocalDate at) {
        String sql = xlate("select * from ByoumeiMaster where name like ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.byoumeiMasterTable);
        String searchText = "%" + text + "%";
        String atString = at.toString();
        return getQuery().query(sql, ts.byoumeiMasterTable, searchText, atString, atString);
    }

    public ByoumeiMasterDTO getByoumeiMasterByName(String name, LocalDate at){
        String sql = xlate("select * from ByoumeiMaster where name = ? " +
                " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.byoumeiMasterTable);
        String atString = at.toString();
        return getQuery().get(sql, ts.byoumeiMasterTable, name, atString, atString);
    }

    // ShuushokugoMaster /////////////////////////////////////////////////////////////////

    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(String text, LocalDate at) {
        String sql = xlate("select * from ShuushokugoMaster where name like ?",
                ts.shuushokugoMasterTable);
        String searchText = "%" + text + "%";
        return getQuery().query(sql, ts.shuushokugoMasterTable, searchText);
    }

    public ShuushokugoMasterDTO getShuushokugoMasterByName(String name){
        String sql = xlate("select * from ShuushokugoMaster where name = ?",
                ts.shuushokugoMasterTable);
        return getQuery().get(sql, ts.shuushokugoMasterTable, name);
    }

    // PrescExample //////////////////////////////////////////////////////////////////////

    public void enterPrescExample(PrescExampleDTO prescExample) {
        ts.prescExampleTable.insert(prescExample);
    }

    public void deletePrescExample(int prescExampleId){
        ts.prescExampleTable.delete(prescExampleId);
    }

    public void updatePrescExample(PrescExampleDTO prescExample){
        ts.prescExampleTable.update(prescExample);
    }

    public List<PrescExampleFullDTO> searchPrescExample(String text) {
        String sql = xlate("select p.*, m.* from PrescExample p, IyakuhinMaster m where " +
                        " m.iyakuhincode = p.iyakuhincode and m.validFrom = p.masterValidFrom " +
                        " and m.name like ? ",
                ts.prescExampleTable, "p", ts.iyakuhinMasterTable, "m");
        String searchText = "%" + text + "%";
        return getQuery().query(sql,
                biProjector(ts.prescExampleTable, ts.iyakuhinMasterTable, PrescExampleFullDTO::create),
                searchText);
    }

    public List<PrescExampleFullDTO> listAllPrescExample() {
        String sql = xlate("select p.*, m.* from PrescExample p, IyakuhinMaster m where " +
                        " m.iyakuhincode = p.iyakuhincode and m.validFrom = p.masterValidFrom ",
                ts.prescExampleTable, "p", ts.iyakuhinMasterTable, "m");
        return getQuery().query(sql,
                biProjector(ts.prescExampleTable, ts.iyakuhinMasterTable, PrescExampleFullDTO::create));
    }

    // PracticeLog ///////////////////////////////////////////////////////////////////////

    private void enterPracticeLog(PracticeLogDTO practiceLog) {
        ts.practiceLogTable.insert(practiceLog);
    }

    public PracticeLogDTO getLastPracticeLog() {
        String sql = xlate("select * from PracticeLog order by serialId desc limit 1",
                ts.practiceLogTable);
        return getQuery().get(sql, ts.practiceLogTable);
    }

    public int getLastPracticeLogId() {
        PracticeLogDTO plog = getLastPracticeLog();
        return plog == null ? 0 : plog.serialId;
    }

    public List<PracticeLogDTO> listPracticeLogSince(int afterThisId) {
        String sql = xlate("select * from PracticeLog where serialId > ? ",
                ts.practiceLogTable);
        return getQuery().query(sql, ts.practiceLogTable, afterThisId);
    }

}
