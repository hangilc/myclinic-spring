package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.backenddb.annotation.ExcludeFromFrontend;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static jp.chang.myclinic.backenddb.Query.NullableProjector;
import static jp.chang.myclinic.backenddb.Query.Projector;
import static jp.chang.myclinic.backenddb.SqlTranslator.TableInfo;

public class Backend {

    private TableSet ts;
    private Query query;
    private SqlTranslator sqlTranslator = new SqlTranslator();
    private PracticeLogger practiceLogger;
    private HotlineLogger hotlineLogger;
    private Projector<DrugAttrDTO> nullableDrugAttrProjector;
    private Projector<ShinryouAttrDTO> nullableShinryouAttrProjector;
    private String forUpdate;

    public Backend(TableSet ts, Query query) {
        this.ts = ts;
        this.query = query;
        this.practiceLogger = new PracticeLogger();
        practiceLogger.setSaver(this::enterPracticeLog);
        this.hotlineLogger = new HotlineLogger();
        this.nullableDrugAttrProjector = new NullableProjector<>(
                ts.drugAttrTable,
                attr -> attr.drugId == 0
        );
        this.nullableShinryouAttrProjector = new NullableProjector<>(
                ts.shinryouAttrTable,
                attr -> attr.shinryouId == 0
        );
        this.forUpdate = ts.dialect.forUpdate();
    }

    private static <S, T, U> Projector<U> biProjector(Projector<S> p1, Projector<T> p2, BiFunction<S, T, U> f) {
        return (rs, ctx) -> {
            S s = p1.project(rs, ctx);
            T t = p2.project(rs, ctx);
            return f.apply(s, t);
        };
    }

    private Projector<Integer> intProjector =
            (rs, ctx) -> rs.getInt(ctx.nextIndex());

    @ExcludeFromFrontend
    public void setPracticeLogPublisher(Consumer<String> publisher) {
        practiceLogger.setPublisher(publisher::accept);
    }

    @ExcludeFromFrontend
    public void setHotlineLogPublisher(Consumer<String> publisher) {
        hotlineLogger.setHotlineLogPublisher(publisher::accept);
    }

    @ExcludeFromFrontend
    public Query getQuery() {
        return query;
    }

    @ExcludeFromFrontend
    public String xlate(String sqlOrig, TableInfo tableInfo) {
        return sqlTranslator.translate(sqlOrig, tableInfo);
    }

    @ExcludeFromFrontend
    private String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                        TableInfo tableInfo2, String alias2) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2);
    }

    @ExcludeFromFrontend
    private String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
                        TableInfo tableInfo2, String alias2, TableInfo tableInfo3, String alias3) {
        return sqlTranslator.translate(sqlOrig, tableInfo1, alias1, tableInfo2, alias2,
                tableInfo3, alias3);
    }

    @ExcludeFromFrontend
    private String xlate(String sqlOrig, TableInfo tableInfo1, String alias1,
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
        PatientDTO prev = ts.patientTable.getByIdForUpdate(patient.patientId, ts.dialect.forUpdate());
        if( prev == null ){
            throw new RuntimeException("Cannot find previous patient to update: " + patient);
        }
        ts.patientTable.update(patient);
        practiceLogger.logPatientUpdated(prev, patient);
    }

    public List<PatientDTO> searchPatientByKeyword2(String lastNameKeyword, String firstNameKeyword) {
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
            return searchPatientByKeyword2(last, first);
        }
    }

    // Visit ////////////////////////////////////////////////////////////////////////

    void enterVisit(VisitDTO visit) {
        ts.visitTable.insert(visit);
        practiceLogger.logVisitCreated(visit);
    }

    public VisitDTO getVisit(int visitId) {
        return ts.visitTable.getById(visitId);
    }

    private void updateVisit(VisitDTO visit) {
        VisitDTO prev = ts.visitTable.getByIdForUpdate(visit.visitId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("Cannot find previous visit to update: " + visit);
        }
        ts.visitTable.update(visit);
        practiceLogger.logVisitUpdated(prev, visit);
    }

    void deleteVisit(int visitId) {
        VisitDTO visit = ts.visitTable.getByIdForUpdate(visitId, ts.dialect.forUpdate());
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
                ts.visitTable);
        return getQuery().get(sql, intProjector, patientId);
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
        return getVisitFull(visit);
    }

    public VisitFull2DTO getVisitFull2(int visitId) {
        VisitDTO visit = getVisit(visitId);
        return getVisitFull2(visit);
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

    // Charge /////////////////////////////////////////////////////////////////////////////

    void enterCharge(ChargeDTO charge) {
        ts.chargeTable.insert(charge);
        practiceLogger.logChargeCreated(charge);
    }

    void updateCharge(ChargeDTO charge){
        ChargeDTO prev = ts.chargeTable.getByIdForUpdate(charge.visitId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("No previous charge to update: " + charge);
        }
        ts.chargeTable.update(charge);
        practiceLogger.logChargeUpdated(prev, charge);
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

    void enterPayment(PaymentDTO payment) {
        ts.paymentTable.insert(payment);
        practiceLogger.logPaymentCreated(payment);
    }

    // Wqueue /////////////////////////////////////////////////////////////////////////////

    void enterWqueue(WqueueDTO wqueue) {
        ts.wqueueTable.insert(wqueue);
        practiceLogger.logWqueueCreated(wqueue);
    }

    void updateWqueue(WqueueDTO wqueue){
        WqueueDTO prev = ts.wqueueTable.getByIdForUpdate(wqueue.visitId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("cannot find previous wqueue: " + wqueue);
        }
        ts.wqueueTable.update(wqueue);
        practiceLogger.logWqueueUpdated(prev, wqueue);
    }

    public WqueueDTO getWqueue(int visitId) {
        return ts.wqueueTable.getById(visitId);
    }

    public void deleteWqueue(int visitId) {
        WqueueDTO wqueue = ts.wqueueTable.getByIdForUpdate(visitId, ts.dialect.forUpdate());
        if (wqueue != null) {
            ts.wqueueTable.delete(wqueue.visitId);
            practiceLogger.logWqueueDeleted(wqueue);
        }
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

    public List<WqueueFullDTO> listWqueueFullForExam() {
        List<Integer> examStates = List.of(
                WqueueWaitState.WaitExam.getCode(),
                WqueueWaitState.InExam.getCode(),
                WqueueWaitState.WaitReExam.getCode()
        );
        return listWqueue().stream().filter(wq -> examStates.contains(wq.waitState))
                .map(this::composeWqueueFullDTO).collect(toList());
    }


    // Hoken //////////////////////////////////////////////////////////////////////////////

    public HokenDTO getHoken(int visitId) {
        VisitDTO visit = getVisit(visitId);
        return getHoken(visit);
    }

    private HokenDTO getHoken(VisitDTO visit) {
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
        VisitDTO origVisit = ts.visitTable.getByIdForUpdate(visit.visitId, ts.dialect.forUpdate());
        VisitDTO updated = VisitDTO.copy(origVisit);
        updated.shahokokuhoId = visit.shahokokuhoId;
        updated.koukikoureiId = visit.koukikoureiId;
        updated.roujinId = visit.roujinId;
        updated.kouhi1Id = visit.kouhi1Id;
        updated.kouhi2Id = visit.kouhi2Id;
        updated.kouhi3Id = visit.kouhi3Id;
        updateVisit(updated);
    }

    // Drug ///////////////////////////////////////////////////////////////////////////

    public DrugDTO getDrug(int drugId) {
        return ts.drugTable.getById(drugId);
    }

    public DrugWithAttrDTO getDrugWithAttr(int drugId) {
        DrugWithAttrDTO result = new DrugWithAttrDTO();
        result.drug = getDrug(drugId);
        result.attr = getDrugAttr(drugId);
        return result;
    }

    int countDrugForVisit(int visitId) {
        String sql = "select count(*) from Drug where visitId = ?";
        return getQuery().get(xlate(sql, ts.drugTable), intProjector, visitId);
    }

    void enterDrug(DrugDTO drug) {
        ts.drugTable.insert(drug);
        practiceLogger.logDrugCreated(drug);
    }

    void updateDrug(DrugDTO drug) {
        DrugDTO prev = ts.drugTable.getByIdForUpdate(drug.drugId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("Cannot find prev drug while updating: " + drug);
        }
        ts.drugTable.update(drug);
        practiceLogger.logDrugUpdated(prev, drug);
    }

    public int batchUpdateDrugDays(List<Integer> drugIds, int days) {
        int count = 0;
        for(Integer drugId: drugIds){
            DrugDTO prev = ts.drugTable.getByIdForUpdate(drugId, ts.dialect.forUpdate());
            if( prev.category == DrugCategory.Naifuku.getCode() && prev.days != days ) {
                DrugDTO drug = DrugDTO.copy(prev);
                drug.days = days;
                updateDrug(drug);
                count += 1;
            }
        }
        return count;
    }

    public int markDrugsAsPrescribed(int visitId) {
        String sql = "select * from Drug where visitId = ? " + forUpdate;
        List<DrugDTO> drugs = getQuery().query(xlate(sql, ts.drugTable), ts.drugTable, visitId);
        int count = 0;
        for(DrugDTO drug: drugs){
            if( drug.prescribed == 0 ) {
                DrugDTO updated = DrugDTO.copy(drug);
                updated.prescribed = 1;
                updateDrug(updated);
                count += 1;
            }
        }
        return count;
    }

    void deleteDrug(int drugId) {
        DrugDTO drug = ts.drugTable.getByIdForUpdate(drugId, ts.dialect.forUpdate());
        if (drug != null) {
            ts.drugTable.delete(drug.drugId);
            practiceLogger.logDrugDeleted(drug);
        }
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

    public DrugFullWithAttrDTO getDrugFullWithAttr(int drugId) {
        DrugFullWithAttrDTO result = new DrugFullWithAttrDTO();
        result.drug = getDrugFull(drugId);
        result.attr = getDrugAttr(drugId);
        return result;
    }

    public List<DrugWithAttrDTO> listDrugWithAttr(int visitId) {
        String sql = xlate("select d.*, a.* from Drug d left join DrugAttr a " +
                        " on d.drugId = a.drugId where d.visitId = ? order by d.drugId",
                ts.drugTable, "d", ts.drugAttrTable, "a");
        return getQuery().query(sql,
                biProjector(ts.drugTable, nullableDrugAttrProjector, DrugWithAttrDTO::create),
                visitId);
    }

    public List<DrugDTO> listDrug(int visitId){
        String sql = "select * from Drug where visitId = ? order by drugId";
        return getQuery().query(xlate(sql, ts.drugTable), ts.drugTable, visitId);
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

    public List<DrugFullDTO> listPrevDrugByPatient(int patientId) {
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

    public int countUnprescribedDrug(int visitId) {
        String sql = xlate("select count(*) from Drug where visitId = ? and prescribed = 0",
                ts.drugTable);
        return getQuery().get(sql, (rs, ctx) -> rs.getInt(ctx.nextIndex()), visitId);
    }

    // DrugAttr /////////////////////////////////////////////////////////////////////////

    public DrugAttrDTO getDrugAttr(int drugId) {
        return ts.drugAttrTable.getById(drugId);
    }

    public void enterDrugAttr(DrugAttrDTO drugAttr) {
        ts.drugAttrTable.insert(drugAttr);
    }

    public void updateDrugAttr(DrugAttrDTO drugAttr) {
        ts.drugAttrTable.update(drugAttr);
    }

    public void deleteDrugAttr(int drugId) {
        ts.drugAttrTable.delete(drugId);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        if (drugIds.size() == 0) {
            return Collections.emptyList();
        } else {
            String sql = "select * from DrugAttr where drugId in (" +
                    drugIds.stream().map(Object::toString).collect(joining(",")) + ")";
            return getQuery().query(xlate(sql, ts.drugAttrTable), ts.drugAttrTable);
        }
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
        TextDTO prev = ts.textTable.getByIdForUpdate(text.textId, ts.dialect.forUpdate());
        if( prev == null ){
            throw new RuntimeException("Cannot find previous text to update: " + text);
        }
        ts.textTable.update(text);
        practiceLogger.logTextUpdated(prev, text);
    }

    public void deleteText(int textId) {
        TextDTO text = ts.textTable.getByIdForUpdate(textId, ts.dialect.forUpdate());
        if( text != null ) {
            ts.textTable.delete(textId);
            practiceLogger.logTextDeleted(text);
        }
    }

    int countTextForVisit(int visitId) {
        String sql = "select count(*) from Text where visitId = ?";
        return getQuery().get(xlate(sql, ts.textTable), intProjector, visitId);
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

    // Shinryou ////////////////////////////////////////////////////////////////////////////

    public ShinryouDTO getShinryou(int shinryouId) {
        return ts.shinryouTable.getById(shinryouId);
    }

    public ShinryouWithAttrDTO getShinryouWithAttr(int shinryouId) {
        ShinryouWithAttrDTO result = new ShinryouWithAttrDTO();
        result.shinryou = getShinryou(shinryouId);
        result.attr = getShinryouAttr(shinryouId);
        return result;
    }

    int countShinryouForVisit(int visitId) {
        String sql = "select count(*) from Shinryou where visitId = ?";
        return getQuery().get(xlate(sql, ts.shinryouTable), intProjector, visitId);
    }

    void enterShinryou(ShinryouDTO shinryou) {
        ts.shinryouTable.insert(shinryou);
        practiceLogger.logShinryouCreated(shinryou);
    }

    void deleteShinryou(int shinryouId) {
        ShinryouDTO shinryou = ts.shinryouTable.getByIdForUpdate(shinryouId, ts.dialect.forUpdate());
        if( shinryou != null ) {
            ts.shinryouTable.delete(shinryouId);
            practiceLogger.logShinryouDeleted(shinryou);
        }
    }

    public ShinryouFullDTO getShinryouFull(int shinryouId) {
        String sql = xlate("select s.*, m.* from Shinryou s, ShinryouMaster m, Visit v " +
                        " where s.shinryouId = ? and s.visitId = v.visitId and s.shinryoucode = m.shinryoucode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt"),
                ts.shinryouTable, "s", ts.shinryouMasterTable, "m", ts.visitTable, "v");
        return getQuery().get(sql,
                biProjector(ts.shinryouTable, ts.shinryouMasterTable, ShinryouFullDTO::create),
                shinryouId);
    }

    public ShinryouFullWithAttrDTO getShinryouFullWithAttr(int shinryouId) {
        ShinryouFullWithAttrDTO result = new ShinryouFullWithAttrDTO();
        result.shinryou = getShinryouFull(shinryouId);
        result.attr = getShinryouAttr(shinryouId);
        return result;
    }

    public List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(this::getShinryouFull).collect(toList());
    }

    public List<ShinryouFullWithAttrDTO> listShinryouFullWithAttrByIds(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(this::getShinryouFullWithAttr).collect(toList());
    }

    public List<ShinryouDTO> listShinryou(int visitId) {
        String sql = xlate("select * from Shinryou where visitId = ? order by shinryouId",
                ts.shinryouTable);
        return getQuery().query(sql, ts.shinryouTable, visitId);
    }

    public List<ShinryouWithAttrDTO> listShinryouWithAttr(int visitId) {
        String sql = xlate("select s.*, a.* from Shinryou s left join ShinryouAttr a " +
                        " on s.shinryouId = a.shinryouId" +
                        " where s.visitId = ? order by s.shinryoucode",
                ts.shinryouTable, "s", ts.shinryouAttrTable, "a");
        return getQuery().query(sql,
                biProjector(ts.shinryouTable, nullableShinryouAttrProjector, ShinryouWithAttrDTO::create),
                visitId);
    }

    public List<ShinryouFullDTO> listShinryouFull(int visitId) {
        String sql = xlate("select s.*, m.* from Shinryou s, ShinryouMaster m, Visit v " +
                        " where s.visitId = ? and s.visitId = v.visitId and s.shinryoucode = m.shinryoucode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by s.shinryoucode",
                ts.shinryouTable, "s", ts.shinryouMasterTable, "m", ts.visitTable, "v");
        return getQuery().query(sql,
                biProjector(ts.shinryouTable, ts.shinryouMasterTable, ShinryouFullDTO::create),
                visitId);
    }

    public List<ShinryouFullWithAttrDTO> listShinryouFullWithAttr(int visitId) {
        String sql = xlate("select s.*, m.*, a.* from Shinryou s, ShinryouMaster m, Visit v" +
                        " left join ShinryouAttr a on s.shinryouId = a.shinryouId " +
                        " where s.visitId = ? and s.visitId = v.visitId and s.shinryoucode = m.shinryoucode " +
                        " and " + ts.dialect.isValidAt("m.validFrom", "m.validUpto", "v.visitedAt") +
                        " order by s.shinryoucode",
                ts.shinryouTable, "s", ts.shinryouMasterTable, "m", ts.visitTable, "v",
                ts.shinryouAttrTable, "a");
        return getQuery().query(sql,
                biProjector(biProjector(ts.shinryouTable, ts.shinryouMasterTable, ShinryouFullDTO::create),
                        nullableShinryouAttrProjector,
                        ShinryouFullWithAttrDTO::create),
                visitId);
    }

    // ShinryouAttr /////////////////////////////////////////////////////////////////////////////

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouIds.stream().map(ts.shinryouAttrTable::getById).filter(Objects::nonNull).collect(toList());
    }

    public ShinryouAttrDTO getShinryouAttr(int shinryouId) {
        return ts.shinryouAttrTable.getById(shinryouId);
    }

    public void enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        ts.shinryouAttrTable.insert(shinryouAttr);
    }

    void deleteShinryouAttr(int shinryouId) {
        ts.shinryouAttrTable.delete(shinryouId);
    }

    void updateShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        ts.shinryouAttrTable.update(shinryouAttr);
    }

    // Conduct ///////////////////////////////////////////////////////////////////////////////

    public void enterConduct(ConductDTO conduct) {
        ts.conductTable.insert(conduct);
        practiceLogger.logConductCreated(conduct);
    }

    int countConductForVisit(int visitId) {
        String sql = "select count(*) from Conduct where visitId = ?";
        return getQuery().get(xlate(sql, ts.conductTable), intProjector, visitId);
    }

    public ConductDTO getConduct(int conductId) {
        return ts.conductTable.getById(conductId);
    }

    void deleteConduct(int conductId) {
        ConductDTO conduct = ts.conductTable.getByIdForUpdate(conductId, ts.dialect.forUpdate());
        if (conduct != null) {
            ts.conductTable.delete(conduct.conductId);
            practiceLogger.logConductDeleted(conduct);
        }
    }

    private void updateConduct(ConductDTO prev, ConductDTO conduct) {
        ts.conductTable.update(conduct);
        practiceLogger.logConductUpdated(prev, conduct);
    }

    public void modifyConductKind(int conductId, int conductKind) {
        ConductDTO prev = ts.conductTable.getByIdForUpdate(conductId, forUpdate);
        ConductDTO updated = ConductDTO.copy(prev);
        updated.kind = conductKind;
        updateConduct(prev, updated);
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
        if( conduct == null ){
            return null;
        }
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
        GazouLabelDTO deleted = ts.gazouLabelTable.getByIdForUpdate(conductId, forUpdate);
        if (deleted != null) {
            ts.gazouLabelTable.delete(deleted.conductId);
            practiceLogger.logGazouLabelDeleted(deleted);
        }
    }

    public void updateGazouLabel(GazouLabelDTO gazouLabel) {
        GazouLabelDTO prev = ts.gazouLabelTable.getByIdForUpdate(gazouLabel.conductId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("Cannot find previous gazou label to update: " + gazouLabel);
        }
        ts.gazouLabelTable.update(gazouLabel);
        practiceLogger.logGazouLabelUpdated(prev, gazouLabel);
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
        ConductShinryouDTO deleted = ts.conductShinryouTable.getByIdForUpdate(conductShinryouId, forUpdate);
        if (deleted != null) {
            ts.conductShinryouTable.delete(deleted.conductShinryouId);
            practiceLogger.logConductShinryouDeleted(deleted);
        }
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
        if (deleted != null) {
            ts.conductDrugTable.delete(deleted.conductDrugId);
            practiceLogger.logConductDrugDeleted(deleted);
        }
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
        ConductKizaiDTO deleted = ts.conductKizaiTable.getByIdForUpdate(conductKizaiId, forUpdate);
        if (deleted != null) {
            ts.conductKizaiTable.delete(deleted.conductKizaiId);
            practiceLogger.logConductKizaiDeleted(deleted);
        }
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

    public void enterShahokokuho(ShahokokuhoDTO shahokokuho) {
        ts.shahokokuhoTable.insert(shahokokuho);
        practiceLogger.logShahokokuhoCreated(shahokokuho);
    }

    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at) {
        String sql = xlate("select * from Shahokokuho where patientId = ? and " +
                        ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.shahokokuhoTable);
        return getQuery().query(sql, ts.shahokokuhoTable, patientId, at, at);
    }

    // Koukikourei //////////////////////////////////////////////////////////////////////////////

    public KoukikoureiDTO getKoukikourei(int koukikoureiId) {
        return ts.koukikoureiTable.getById(koukikoureiId);
    }

    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at) {
        String sql = xlate("select * from Koukikourei where patientId = ? and " +
                        ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.koukikoureiTable);
        return getQuery().query(sql, ts.koukikoureiTable, patientId, at, at);
    }

    // Roujin ////////////////////////////////////////////////////////////////////////////////////

    public RoujinDTO getRoujin(int roujinId) {
        return ts.roujinTable.getById(roujinId);
    }

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at) {
        String sql = xlate("select * from Roujin where patientId = ? and " +
                        ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.roujinTable);
        return getQuery().query(sql, ts.roujinTable, patientId, at, at);
    }

    // Kouhi //////////////////////////////////////////////////////////////////////////////////////

    public KouhiDTO getKouhi(int kouhiId) {
        return ts.kouhiTable.getById(kouhiId);
    }

    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at) {
        String sql = xlate("select * from Kouhi where patientId = ? and " +
                        ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.kouhiTable);
        return getQuery().query(sql, ts.kouhiTable, patientId, at, at);
    }

    // Disease ////////////////////////////////////////////////////////////////////////////////////

    public void enterDisease(DiseaseDTO disease) {
        ts.diseaseTable.insert(disease);
        practiceLogger.logDiseaseCreated(disease);
    }

    public void enterNewDisease(DiseaseNewDTO disease) {
        enterDisease(disease.disease);
        int diseaseId = disease.disease.diseaseId;
        if (disease.adjList != null) {
            disease.adjList.forEach(adj -> {
                adj.diseaseId = diseaseId;
                enterDiseaseAdj(adj);
            });
        }
    }

    public DiseaseDTO getDisease(int diseaseId) {
        return ts.diseaseTable.getById(diseaseId);
    }

    public void updateDisease(DiseaseDTO disease) {
        DiseaseDTO prev = ts.diseaseTable.getByIdForUpdate(disease.diseaseId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("Cannot find previous disease to update: "+ disease);
        }
        ts.diseaseTable.update(disease);
        practiceLogger.logDiseaseUpdated(prev, disease);
    }

    public void deleteDisease(int diseaseId) {
        DiseaseDTO deleted = ts.diseaseTable.getByIdForUpdate(diseaseId, forUpdate);
        if( deleted != null ) {
            ts.diseaseTable.delete(diseaseId);
            practiceLogger.logDiseaseDeleted(deleted);
        }
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
            DiseaseDTO prev = ts.diseaseTable.getByIdForUpdate(modify.diseaseId, forUpdate);
            DiseaseDTO d = DiseaseDTO.copy(prev);
            d.endDate = modify.endDate;
            d.endReason = modify.endReason;
            updateDisease(d);
        }
    }

    // DiseaseAdj ////////////////////////////////////////////////////////////////////////

    public void enterDiseaseAdj(DiseaseAdjDTO adj) {
        ts.diseaseAdjTable.insert(adj);
        practiceLogger.logDiseaseAdjCreated(adj);
    }

    public void deleteDiseaseAdj(int diseaseAdjId) {
        DiseaseAdjDTO adj = ts.diseaseAdjTable.getByIdForUpdate(diseaseAdjId, forUpdate);
        if (adj != null) {
            ts.diseaseAdjTable.delete(adj.diseaseAdjId);
            practiceLogger.logDiseaseAdjDeleted(adj);
        }
    }

    public List<DiseaseAdjDTO> listDiseaseAdj(int diseaseId){
        String sql = "select * from DiseaseAdj where diseaseId = ?";
        return getQuery().query(xlate(sql, ts.diseaseAdjTable), ts.diseaseAdjTable, diseaseId);
    }

    public int deleteDiseaseAdjForDisease(DiseaseDTO disease){
        int count = 0;
        String sql = "select * from DiseaseAdj where diseaseId = ? " + forUpdate;
        List<DiseaseAdjDTO> adjList = getQuery().query(xlate(sql, ts.diseaseAdjTable), ts.diseaseAdjTable,
                disease.diseaseId);
        for(DiseaseAdjDTO adj: adjList){
            deleteDiseaseAdj(adj.diseaseAdjId);
            count += 1;
        }
        return count;
    }

    // PharmaQueue ///////////////////////////////////////////////////////////////////////

    public PharmaQueueDTO getPharmaQueue(int visitId) {
        return ts.pharmaQueueTable.getById(visitId);
    }

    public void enterPharmaQueue(PharmaQueueDTO pharmaQueue) {
        ts.pharmaQueueTable.insert(pharmaQueue);
        practiceLogger.logPharmaQueueCreated(pharmaQueue);
    }

    void updatePharmaQueue(PharmaQueueDTO pharmaQueue){
        PharmaQueueDTO prev = ts.pharmaQueueTable.getByIdForUpdate(pharmaQueue.visitId, forUpdate);
        if( prev == null ){
            throw new RuntimeException("Cannot find prev to update PharmaQueue: " + pharmaQueue);
        }
        ts.pharmaQueueTable.update(pharmaQueue);
        practiceLogger.logPharmaQueueUpdated(prev, pharmaQueue);
    }

    public void deletePharmaQueue(int visitId) {
        PharmaQueueDTO pharmaQueue = ts.pharmaQueueTable.getByIdForUpdate(visitId, forUpdate);
        if( pharmaQueue != null ) {
            ts.pharmaQueueTable.delete(pharmaQueue.visitId);
            practiceLogger.logPharmaQueueDeleted(pharmaQueue);
        }
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

    public ShinryouMasterDTO getShinryouMaster(int shinryoucode, LocalDate at) {
        String sql = xlate("select * from ShinryouMaster " +
                        " where shinryoucode = ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.shinryouMasterTable);
        String atString = at.toString();
        return getQuery().get(sql, ts.shinryouMasterTable, shinryoucode, atString, atString);
    }

    // IyakuhinMaster /////////////////////////////////////////////////////////////////////

    public IyakuhinMasterDTO getIyakuhinMaster(int iyakuhincode, LocalDate at) {
        String sql = xlate("select * from IyakuhinMaster where iyakuhincode = ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.iyakuhinMasterTable);
        String atString = at.toString();
        return getQuery().get(sql, ts.iyakuhinMasterTable, iyakuhincode, atString, atString);
    }

    public List<IyakuhinMasterDTO> searchIyakuhinMaster(String text, LocalDate at) {
        String sql = xlate("select * from IyakuhinMaster where name like ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.iyakuhinMasterTable);
        String searchText = "%" + text + "%";
        String atString = at.toString();
        return getQuery().query(sql, ts.iyakuhinMasterTable, searchText, atString, atString);
    }

    // KizaiMaster ///////////////////////////////////////////////////////////////////////

    public KizaiMasterDTO getKizaiMaster(int kizaicode, LocalDate at) {
        String sql = xlate("select * from KizaiMaster where kizaicode = ? " +
                        " and " + ts.dialect.isValidAt("validFrom", "validUpto", "?"),
                ts.kizaiMasterTable);
        String atString = at.toString();
        return getQuery().get(sql, ts.kizaiMasterTable, kizaicode, atString, atString);
    }

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

    public ByoumeiMasterDTO getByoumeiMasterByName(String name, LocalDate at) {
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

    public ShuushokugoMasterDTO getShuushokugoMasterByName(String name) {
        String sql = xlate("select * from ShuushokugoMaster where name = ?",
                ts.shuushokugoMasterTable);
        return getQuery().get(sql, ts.shuushokugoMasterTable, name);
    }

    // PrescExample //////////////////////////////////////////////////////////////////////

    public void enterPrescExample(PrescExampleDTO prescExample) {
        ts.prescExampleTable.insert(prescExample);
    }

    public void deletePrescExample(int prescExampleId) {
        ts.prescExampleTable.delete(prescExampleId);
    }

    public void updatePrescExample(PrescExampleDTO prescExample) {
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

    public List<PracticeLogDTO> listAllPracticeLog(LocalDate date){
        String sql = xlate("select * from PracticeLog where date(createdAt) = ? order by serialId",
                ts.practiceLogTable);
        return getQuery().query(sql, ts.practiceLogTable, date.toString());
    }

}
