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
    public void testEnter() {
        int logIndex = getCurrentPracticeLogIndex();
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
        confirmSingleLog(logIndex, log -> log.getDrugCreated()
                .map(drugCreated -> drugCreated.created.equals(drug))
                .orElse(false));
        confirmSingleLog(logIndex, log -> log.getPharmaQueueCreated()
                .map(pharmaQueueCreated -> pharmaQueueCreated.created.visitId == visit.visitId)
                .orElse(false));
    }

    @DbTest
    public void testUpdate() {
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        dbBackend.txProc(b -> b.enterDrug(drug));
        DrugDTO updated = DrugDTO.copy(drug);
        updated.iyakuhincode = SampleData.loxonin.iyakuhincode;
        updated.amount = 1;
        updated.usage = "頭痛時";
        updated.days = 10;
        updated.category = DrugCategory.Tonpuku.getCode();
        dbBackend.txProc(b -> b.updateDrug(updated));
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(updated.equals(drugs.get(0).drug), "testUpdate", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
        confirmSingleLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> {
                    DrugDTO prev = drugUpdated.prev;
                    DrugDTO updatedDrug = drugUpdated.updated;
                    return prev.equals(drug) && updatedDrug.equals(updated);
                })
                .orElse(false));
        confirmSingleLog(logIndex, log -> log.getPharmaQueueCreated()
                .map(pharmaQueueCreated -> pharmaQueueCreated.created.visitId == visit.visitId)
                .orElse(false));
    }

    @DbTest
    public void testDelete(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        dbBackend.txProc(b -> b.enterDrug(drug));
        dbBackend.txProc(b -> b.deleteDrug(drug.drugId));
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 0);
        confirmSingleLog(logIndex, log -> log.getDrugDeleted()
                .map(drugDeleted -> drugDeleted.deleted.equals(drug))
                .orElse(false));
        confirmNoLog(logIndex, log -> log.getPharmaQueueCreated()
                .map(pharmaQueueCreated -> pharmaQueueCreated.created.visitId == visit.visitId)
                .orElse(false));
    }

}
