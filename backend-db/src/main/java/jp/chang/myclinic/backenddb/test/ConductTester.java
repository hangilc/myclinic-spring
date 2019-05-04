package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mockdata.SampleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ConductTester extends TesterBase {


    ConductTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void enterConductFull(){
        VisitDTO visit = startVisit();
        ConductEnterRequestDTO conductReq = new ConductEnterRequestDTO();
        conductReq.shinryouList = new ArrayList<>();
        conductReq.drugs = new ArrayList<>();
        conductReq.kizaiList = new ArrayList<>();
        conductReq.visitId = visit.visitId;
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = "骨塩定量に使用";
        {
            ConductShinryouDTO shinryou = new ConductShinryouDTO();
            shinryou.shinryoucode = 160147310; // 骨塩定量検査（ＭＤ法、ＳＥＸＡ法等）
            conductReq.shinryouList.add(shinryou);
        }
        {
            ConductDrugDTO drug = new ConductDrugDTO();
            drug.iyakuhincode = SampleData.calonal.iyakuhincode;
            drug.amount = 1.0;
            conductReq.drugs.add(drug);
        }
        {
            ConductKizaiDTO kizai = new ConductKizaiDTO();
            kizai.kizaicode = 700080000; // 四ツ切
            kizai.amount = 1.0;
            conductReq.kizaiList.add(kizai);
        }
        ConductFullDTO enteredConduct = dbBackendService.enterConductFull(conductReq);
        int conductId = enteredConduct.conduct.conductId;
        {
            ConductDTO conduct = enteredConduct.conduct;
            confirm(conduct.visitId == visit.visitId);
            confirm(conduct.kind == ConductKind.Gazou.getCode());
        }
        {
            GazouLabelDTO gazouLabel = enteredConduct.gazouLabel;
            confirm(gazouLabel.label.equals(conductReq.gazouLabel));
            confirm(gazouLabel.conductId == conductId);
        }
        {
            List<ConductShinryouFullDTO> shinryouList = enteredConduct.conductShinryouList;
            confirm(shinryouList.size() == 1);
            ConductShinryouFullDTO shinryouFull = shinryouList.get(0);
            confirm(shinryouFull.conductShinryou.shinryoucode == 160147310);
            confirm(shinryouFull.master.shinryoucode == 160147310);
        }
        {
            List<ConductDrugFullDTO> drugList = enteredConduct.conductDrugs;
            confirm(drugList.size() == 1);
            ConductDrugFullDTO drugFull = drugList.get(0);
            confirm(drugFull.conductDrug.iyakuhincode == SampleData.calonal.iyakuhincode);
            confirm(drugFull.master.iyakuhincode == SampleData.calonal.iyakuhincode);
            confirm(drugFull.conductDrug.amount == 1.0);
        }
        {
            List<ConductKizaiFullDTO> kizaiList = enteredConduct.conductKizaiList;
            confirm(kizaiList.size() == 1);
            ConductKizaiFullDTO kizaiFull = kizaiList.get(0);
            confirm(kizaiFull.conductKizai.kizaicode == 700080000);
            confirm(kizaiFull.master.kizaicode == 700080000);
            confirm(kizaiFull.conductKizai.amount == 1.0);
        }
    }

    @DbTest
    public void deleteConduct(){
        VisitDTO visit = startVisit();
        ConductEnterRequestDTO conductReq = new ConductEnterRequestDTO();
        conductReq.shinryouList = new ArrayList<>();
        conductReq.drugs = new ArrayList<>();
        conductReq.kizaiList = new ArrayList<>();
        conductReq.visitId = visit.visitId;
        conductReq.kind = ConductKind.Gazou.getCode();
        conductReq.gazouLabel = "骨塩定量に使用";
        {
            ConductShinryouDTO shinryou = new ConductShinryouDTO();
            shinryou.shinryoucode = 160147310; // 骨塩定量検査（ＭＤ法、ＳＥＸＡ法等）
            conductReq.shinryouList.add(shinryou);
        }
        {
            ConductDrugDTO drug = new ConductDrugDTO();
            drug.iyakuhincode = SampleData.calonal.iyakuhincode;
            drug.amount = 1.0;
            conductReq.drugs.add(drug);
        }
        {
            ConductKizaiDTO kizai = new ConductKizaiDTO();
            kizai.kizaicode = 700080000; // 四ツ切
            kizai.amount = 1.0;
            conductReq.kizaiList.add(kizai);
        }
        ConductFullDTO enteredConduct = dbBackendService.enterConductFull(conductReq);
        int conductId = enteredConduct.conduct.conductId;
        dbBackendService.deleteConduct(conductId);
        confirm(dbBackend.query(backend -> backend.getConductFull(conductId)) == null);
    }
}
