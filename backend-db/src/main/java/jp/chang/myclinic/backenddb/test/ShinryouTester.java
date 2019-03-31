package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class ShinryouTester extends TesterBase {


    ShinryouTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testGetShinryouFull(Backend backend){
        ShinryouFullDTO result = backend.getShinryouFull(2012);
        System.out.println(result);
    }

    @DbTest
    public void testFindShinryouMasterByName(Backend backend){
        Map<String, Integer> map = backend.batchResolveShinryouNames(
                List.of(List.of("初診", "初診", "初診料"), List.of("再診料")), LocalDate.of(2019, 3, 31));
        System.out.println(map);
    }

    @DbTest
    public void testEnterKotsuenTeiryou(Backend backend){
        LocalDateTime now = LocalDateTime.now();
        LocalDate at = now.toLocalDate();
        VisitDTO visit = backend.startVisit(patient1.patientId, now);
        BatchEnterRequestDTO req = new BatchEnterRequestDTO();
        req.shinryouList = new ArrayList<>();
        req.conducts = new ArrayList<>();
        ConductEnterRequestDTO conductReq = new ConductEnterRequestDTO();
        req.conducts.add(conductReq);
        conductReq.shinryouList = new ArrayList<>();
        conductReq.drugs = new ArrayList<>();
        conductReq.kizaiList = new ArrayList<>();
        conductReq.visitId = visit.visitId;
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = "骨塩定量に使用";
        {
            ShinryouMasterDTO master =
                    backend.resolveShinryouMasterByName(List.of("骨塩定量検査（ＭＤ法、ＳＥＸＡ法等）"), at);
            if( master == null ){
                throw new RuntimeException("Cannot find shinryou master: 骨塩定量ＭＤ法");
            }
            ConductShinryouDTO shinryou = new ConductShinryouDTO();
            shinryou.shinryoucode = master.shinryoucode;
            conductReq.shinryouList.add(shinryou);
        }
        {
            KizaiMasterDTO master = backend.resolveKizaiMasterByName(List.of("四ツ切"), at);
            if( master == null ){
                throw new RuntimeException("Cannot find kizai master: 四ツ切");
            }
            ConductKizaiDTO kizai = new ConductKizaiDTO();
            kizai.kizaicode = master.kizaicode;
            kizai.amount = 1.0;
            conductReq.kizaiList.add(kizai);
        }
        BatchEnterResultDTO result = backend.batchEnter(req);
        //System.out.println(result);
        int conductId = result.conductIds.get(0);
        ConductFullDTO entered = backend.getConductFull(conductId);
        //System.out.println(entered);
    }

}
