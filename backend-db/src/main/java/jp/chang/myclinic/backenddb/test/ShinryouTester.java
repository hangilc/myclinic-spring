package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouWithAttrDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

class ShinryouTester extends TesterBase {

    ShinryouTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    private static int 初診 = 111000110;
    private static int 再診 = 112007410;
    private static int 処方料 = 120001210;
    private static int 処方せん料 = 120002910;
    private static int 調基 = 120001810;
    private static int 特定疾患処方 = 120002270;
    private static int 特定疾患処方管理加算処方せん料 = 120002570;
    private static int 長期処方 = 120003170;
    private static int 長期投薬加算処方せん料 = 120003270;
    private static int 特定疾患管理 = 113001810;
    private static int 薬剤情報提供 = 120002370;
    private static int 手帳記載加算 = 113701310;

    private ShinryouDTO createShinryou(int visitId, int shinryoucode){
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visitId;
        shinryou.shinryoucode = shinryoucode;
        return shinryou;
    }

    private ShinryouAttrDTO createAttr(String tekiyou){
        ShinryouAttrDTO attr = new ShinryouAttrDTO();
        attr.tekiyou = tekiyou;
        return attr;
    }

    private void enter(ShinryouDTO shinryou, ShinryouAttrDTO attr){
        ShinryouWithAttrDTO withAttr = new ShinryouWithAttrDTO();
        withAttr.shinryou = shinryou;
        withAttr.attr = attr;
        dbBackendService.enterShinryouWithAttr(withAttr);
    }

    @DbTest
    public void testEnter(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        ShinryouDTO shinryou = createShinryou(visit.visitId, 初診);
        dbBackendService.enterShinryou(shinryou);
        endExam(visit.visitId, 10);
        confirm(dbBackend.query(backend -> backend.getShinryou(shinryou.shinryouId)).equals(shinryou));
        confirmSingleLog(logIndex, log -> log.getShinryouCreated()
                .map(sc -> sc.created.equals(shinryou))
                .orElse(false));
    }

    @DbTest
    public void testEnterWithAttr(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        ShinryouDTO shinryou = createShinryou(visit.visitId, 初診);
        ShinryouAttrDTO attr = createAttr("摘要テスト");
        enter(shinryou, attr);
        endExam(visit.visitId, 10);
        confirm(dbBackend.query(backend -> backend.getShinryou(shinryou.shinryouId)).equals(shinryou));
        confirm(dbBackend.query(backend -> backend.getShinryouAttr(shinryou.shinryouId).equals(attr)), "enterWithAttr",
                () -> System.out.println(attr));
        confirmSingleLog(logIndex, log -> log.getShinryouCreated()
                .map(sc -> sc.created.equals(shinryou))
                .orElse(false));
    }

    @DbTest
    public void testDeleteDuplicate(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        dbBackendService.enterShinryou(createShinryou(visit.visitId, 初診));
        dbBackendService.enterShinryou(createShinryou(visit.visitId, 再診));
        dbBackendService.enterShinryou(createShinryou(visit.visitId, 再診));
        dbBackendService.enterShinryou(createShinryou(visit.visitId, 調基));
        dbBackendService.enterShinryou(createShinryou(visit.visitId, 調基));
        dbBackendService.deleteDuplicateShinryou(visit.visitId);
        endExam(visit.visitId, 120);
        List<ShinryouDTO> saved = dbBackend.query(backend -> backend.listShinryou(visit.visitId));
        confirm(saved.size() == 3);
        confirm(saved.stream().map(s -> s.shinryoucode).collect(toSet()).equals(Set.of(初診, 再診, 調基)));
    }

    @DbTest
    public void testDeleteDuplicateWithAttr(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        ShinryouDTO sa = createShinryou(visit.visitId, 初診);
        ShinryouDTO sb1 = createShinryou(visit.visitId, 再診);
        ShinryouDTO sb2 = createShinryou(visit.visitId, 再診);
        ShinryouDTO sc1 = createShinryou(visit.visitId, 調基);
        ShinryouDTO sc2 = createShinryou(visit.visitId, 調基);
        ShinryouAttrDTO aa = createAttr("A1");
        ShinryouAttrDTO ab1 = createAttr("B1");
        ShinryouAttrDTO ab2 = createAttr("B2");
        ShinryouAttrDTO ac1 = createAttr("C1");
        ShinryouAttrDTO ac2 = createAttr("C2");
        enter(sa, aa);
        enter(sb1, ab1);
        enter(sb2, ab2);
        enter(sc1, ac1);
        enter(sc2, ac2);
        dbBackendService.deleteDuplicateShinryou(visit.visitId);
        endExam(visit.visitId, 120);
        List<ShinryouDTO> saved = dbBackend.query(backend -> backend.listShinryou(visit.visitId));
        confirm(saved.size() == 3);
        confirm(saved.stream().map(s -> s.shinryoucode).collect(toSet()).equals(Set.of(初診, 再診, 調基)));
        confirm(dbBackend.query(backend -> backend.getShinryouAttr(sa.shinryouId)).equals(aa));
        ShinryouDTO savedSaishin = saved.stream().filter(s -> s.shinryoucode == 再診).findFirst().orElse(null);
        confirmNotNull(savedSaishin);
        if( savedSaishin.shinryouId == sb1.shinryouId ){
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sb1.shinryouId)).equals(ab1));
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sb2.shinryouId)) == null);
        } else {
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sb2.shinryouId)).equals(ab2));
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sb1.shinryouId)) == null);
        }
        ShinryouDTO savedChouki = saved.stream().filter(s -> s.shinryoucode == 調基).findFirst().orElse(null);
        confirmNotNull(savedChouki);
        if( savedChouki.shinryouId == sc1.shinryouId ){
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sc1.shinryouId)).equals(ac1));
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sc2.shinryouId)) == null);
        } else {
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sc2.shinryouId)).equals(ac2));
            confirm(dbBackend.query(backend -> backend.getShinryouAttr(sc1.shinryouId)) == null);
        }
    }

//    @DbTest
//    public void testGetShinryouFull(Backend backend){
//        ShinryouFullDTO result = backend.getShinryouFull(2012);
//        //System.out.println(result);
//    }
//
//    @DbTest
//    public void testFindShinryouMasterByName(Backend backend){
//        Map<String, Integer> map = backend.batchResolveShinryouNames(
//                List.of(List.of("初診", "初診", "初診料"), List.of("再診料")), LocalDate.of(2019, 3, 31));
//    }
//
//    @DbTest
//    public void testEnterKotsuenTeiryou(Backend backend){
//        LocalDateTime now = LocalDateTime.now();
//        LocalDate at = now.toLocalDate();
//        VisitDTO visit = backend.startVisit(patient1.patientId, now);
//        BatchEnterRequestDTO req = new BatchEnterRequestDTO();
//        req.shinryouList = new ArrayList<>();
//        req.conducts = new ArrayList<>();
//        ConductEnterRequestDTO conductReq = new ConductEnterRequestDTO();
//        req.conducts.add(conductReq);
//        conductReq.shinryouList = new ArrayList<>();
//        conductReq.drugs = new ArrayList<>();
//        conductReq.kizaiList = new ArrayList<>();
//        conductReq.visitId = visit.visitId;
//        conductReq.kind = ConductKind.Gazou.getCode();
//        conductReq.gazouLabel = "骨塩定量に使用";
//        {
//            ShinryouMasterDTO master =
//                    backend.resolveShinryouMasterByName(List.of("骨塩定量検査（ＭＤ法、ＳＥＸＡ法等）"), at);
//            if( master == null ){
//                throw new RuntimeException("Cannot find shinryou master: 骨塩定量ＭＤ法");
//            }
//            ConductShinryouDTO shinryou = new ConductShinryouDTO();
//            shinryou.shinryoucode = master.shinryoucode;
//            conductReq.shinryouList.add(shinryou);
//        }
//        {
//            KizaiMasterDTO master = backend.resolveKizaiMasterByName(List.of("四ツ切"), at);
//            if( master == null ){
//                throw new RuntimeException("Cannot find kizai master: 四ツ切");
//            }
//            ConductKizaiDTO kizai = new ConductKizaiDTO();
//            kizai.kizaicode = master.kizaicode;
//            kizai.amount = 1.0;
//            conductReq.kizaiList.add(kizai);
//        }
//        BatchEnterResultDTO result = backend.batchEnter(req);
//        //System.out.println(result);
//        int conductId = result.conductIds.get(0);
//        ConductFullDTO entered = backend.getConductFull(conductId);
//        //System.out.println(entered);
//    }
//
//    @DbTest
//    public void testDeleteDuplicateShinryou(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        int shinryoucode = 120002370;
//        ShinryouDTO shinryou = new ShinryouDTO();
//        shinryou.visitId = visit.visitId;
//        shinryou.shinryoucode = shinryoucode;
//        backend.enterShinryou(shinryou);
//        ShinryouDTO shinryou2 = new ShinryouDTO();
//        shinryou2.visitId = visit.visitId;
//        shinryou2.shinryoucode = shinryoucode;
//        backend.enterShinryou(shinryou2);
//        List<Integer> deleted = backend.deleteDuplicateShinryou(visit.visitId);
//        confirm(backend.listShinryouFull(visit.visitId).size() == 1);
//        confirm(deleted.size() == 1 && deleted.get(0) == shinryou2.shinryouId);
//    }
//
//    @DbTest
//    public void testDeleteDuplicateShinryouPreserveSingle(Backend backend){
//        VisitDTO visit = backend.startVisit(patient1.patientId, LocalDateTime.now());
//        int shinryoucode = 120002370;
//        int shinryoucode2 = 120001810;
//        ShinryouDTO shinryou = new ShinryouDTO();
//        shinryou.visitId = visit.visitId;
//        shinryou.shinryoucode = shinryoucode;
//        backend.enterShinryou(shinryou);
//        ShinryouDTO shinryou2 = new ShinryouDTO();
//        shinryou2.visitId = visit.visitId;
//        shinryou2.shinryoucode = shinryoucode2;
//        backend.enterShinryou(shinryou2);
//        ShinryouDTO shinryou3 = new ShinryouDTO();
//        shinryou3.visitId = visit.visitId;
//        shinryou3.shinryoucode = shinryoucode;
//        backend.enterShinryou(shinryou3);
//        List<Integer> deleted = backend.deleteDuplicateShinryou(visit.visitId);
//        Map<Integer, List<ShinryouFullDTO>> list = backend.listShinryouFull(visit.visitId).stream()
//                .collect(Collectors.groupingBy(s -> s.shinryou.shinryoucode));
//        confirm(list.size() == 2);
//        confirm(list.get(shinryoucode).size() == 1);
//        confirm(list.get(shinryoucode2).size() == 1);
//        confirm(deleted.size() == 1 && deleted.get(0) == shinryou3.shinryouId);
//    }

}
