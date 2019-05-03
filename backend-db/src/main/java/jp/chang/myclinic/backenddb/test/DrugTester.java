package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.SampleData;

import java.time.LocalDateTime;
import java.util.List;

class DrugTester extends TesterBase {

    DrugTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testEnter(){
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        dbBackend.txProc(b -> b.enterDrug(drug));
        endExam(visit.visitId, 0);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(drug.equals(drugs.get(0).drug), "testEnter", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
    }

}
