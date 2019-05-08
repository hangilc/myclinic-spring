package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.support.SupportSet;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SupportService {

    private DbBackend dbBackend;
    private SupportSet ss;

    public SupportService(DbBackend dbBackend, SupportSet ss) {
        this.dbBackend = dbBackend;
        this.ss = ss;
    }

    public ShinryouMasterDTO resolveShinryouMasterByKey(String key, LocalDate at) {
        int shinryoucode = ss.shinryoucodeResolver.resolveShinryoucodeByKey(key);
        if (shinryoucode == 0) {
            return null;
        } else {
            return dbBackend.query(backend -> backend.getShinryouMaster(shinryoucode, at));
        }
    }

    public KizaiMasterDTO resolveKizaiMasterByKey(String key, LocalDate at) {
        int kizaicode = ss.kizaicodeResolver.resolveKizaicodeByKey(key);
        if (kizaicode == 0) {
            return null;
        } else {
            return dbBackend.query(backend -> backend.getKizaiMaster(kizaicode, at));
        }
    }

    public int enterShinryouByName(int visitId, String name){
        VisitDTO visit = dbBackend.query(backend -> backend.getVisit(visitId));
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        ShinryouMasterDTO master = resolveShinryouMasterByKey(name, at);
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visitId;
        shinryou.shinryoucode = master.shinryoucode;
        dbBackend.txProc(backend -> backend.enterShinryou(shinryou));
        return shinryou.shinryouId;
    }

    public BatchEnterResultDTO batchEnterByNames(int visitId, BatchEnterByNamesRequestDTO req) {
        VisitDTO visit = dbBackend.query(backend -> backend.getVisit(visitId));
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        BatchEnterResultDTO result = new BatchEnterResultDTO();
        result.shinryouIds = new ArrayList<>();
        result.conductIds = new ArrayList<>();
        if (req.shinryouNames != null) {
            req.shinryouNames.forEach(key -> {
                ShinryouMasterDTO m = resolveShinryouMasterByKey(key, at);
                if (m == null) {
                    throw new RuntimeException("Cannot find shinryou master: " + key);
                }
                ShinryouDTO shinryou = new ShinryouDTO();
                shinryou.visitId = visitId;
                shinryou.shinryoucode = m.shinryoucode;
                dbBackend.txProc(backend -> backend.enterShinryou(shinryou));
                result.shinryouIds.add(shinryou.shinryouId);
            });
        }
        if (req.conducts != null) {
            req.conducts.forEach(conductReq -> {
                ConductDTO conduct = new ConductDTO();
                conduct.visitId = visitId;
                conduct.kind = conductReq.kind;
                dbBackend.txProc(backend -> backend.enterConduct(conduct));
                int conductId = conduct.conductId;
                if (conductReq.gazouLabel != null) {
                    GazouLabelDTO gazouLabel = new GazouLabelDTO();
                    gazouLabel.conductId = conductId;
                    gazouLabel.label = conductReq.gazouLabel;
                    dbBackend.txProc(backend -> backend.enterGazouLabel(gazouLabel));
                }
                if (conductReq.shinryouNames != null) {
                    conductReq.shinryouNames.forEach(key -> {
                        ShinryouMasterDTO master = resolveShinryouMasterByKey(key, at);
                        if (master == null) {
                            throw new RuntimeException("Cannot find shinryou master: " + key);
                        }
                        ConductShinryouDTO shinryou = new ConductShinryouDTO();
                        shinryou.conductId = conductId;
                        shinryou.shinryoucode = master.shinryoucode;
                        dbBackend.txProc(backend -> backend.enterConductShinryou(shinryou));
                    });
                }
                if (conductReq.kizaiList != null) {
                    conductReq.kizaiList.forEach(kizaiReq -> {
                        KizaiMasterDTO master = resolveKizaiMasterByKey(kizaiReq.name, at);
                        if (master == null) {
                            throw new RuntimeException("Cannot find kizai master: " + kizaiReq.name);
                        }
                        ConductKizaiDTO kizai = new ConductKizaiDTO();
                        kizai.conductId = conductId;
                        kizai.kizaicode = master.kizaicode;
                        kizai.amount = kizaiReq.amount;
                        dbBackend.txProc(backend -> backend.enterConductKizai(kizai));
                    });
                }
                result.conductIds.add(conductId);
            });
        }
        return result;
    }

    public List<DiseaseExampleDTO> listDiseaseExample() {
        return ss.diseaseExampleProvider.listDiseaseExample();
    }

    public MeisaiDTO getMeisai(int visitId) {
        VisitDTO visit = dbBackend.query(backend -> backend.getVisit(visitId));
        LocalDate at = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        return dbBackend.query(backend -> ss.meisaiService.getMeisai(
                backend.getPatient(visit.patientId),
                backend.getHoken(visitId),
                at,
                backend.listShinryouFull(visitId),
                ss.houkatsuKensaService.getRevision(at),
                backend.listDrugFull(visitId),
                backend.listConductFull(visitId)
        ));
    }

    public IyakuhinMasterDTO resolveStockDrug(int iyakuhincode, LocalDate at) {
        final int resolvedIyakuhincode = ss.stockDrugService.resolve(iyakuhincode, at);
        return dbBackend.query(backend -> backend.getIyakuhinMaster(resolvedIyakuhincode, at));
    }

    public List<ResolvedStockDrugDTO> batchResolveStockDrug(List<Integer> iyakuhincodes, LocalDate at) {
        return iyakuhincodes.stream()
                .map(code ->
                        ResolvedStockDrugDTO.create(code, ss.stockDrugService.resolve(code, at)))
                .collect(toList());
    }

    public ClinicInfoDTO getClinicInfo() {
        return ss.clinicInfoProvider.getClinicInfo();
    }

}
