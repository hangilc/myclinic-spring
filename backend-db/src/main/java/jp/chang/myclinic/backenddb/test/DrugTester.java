package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
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
        dbBackendService.enterDrug(drug);
        confirm(dbBackend.query(backend -> backend.getPharmaQueue(visit.visitId)) == null);
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
    public void testEnterWithAttr() {
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        DrugAttrDTO attr = new DrugAttrDTO();
        attr.tekiyou = "摘要のテスト";
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = attr;
        dbBackendService.enterDrugWithAttr(drugWithAttr);
        confirm(dbBackend.query(backend -> backend.getPharmaQueue(visit.visitId)) == null);
        endExam(visit.visitId, 0);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(drug.equals(drugs.get(0).drug), "testEnter", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
        DrugAttrDTO savedAttr = dbBackend.query(b -> b.getDrugAttr(drug.drugId));
        confirmNotNull(savedAttr);
        confirm(savedAttr.tekiyou.equals(attr.tekiyou));
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
        dbBackendService.enterDrug(drug);
        DrugDTO updated = DrugDTO.copy(drug);
        updated.iyakuhincode = SampleData.loxonin.iyakuhincode;
        updated.amount = 1;
        updated.usage = "頭痛時";
        updated.days = 10;
        updated.category = DrugCategory.Tonpuku.getCode();
        dbBackendService.updateDrug(updated);
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
    public void testUpdateWithAttr() {
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        dbBackendService.enterDrug(drug);
        DrugDTO updated = DrugDTO.copy(drug);
        updated.iyakuhincode = SampleData.loxonin.iyakuhincode;
        updated.amount = 1;
        updated.usage = "頭痛時";
        updated.days = 10;
        updated.category = DrugCategory.Tonpuku.getCode();
        DrugAttrDTO updatedAttr = new DrugAttrDTO();
        updatedAttr.drugId = drug.drugId;
        updatedAttr.tekiyou = "新しい摘要";
        DrugWithAttrDTO updatedWithAttr = new DrugWithAttrDTO();
        updatedWithAttr.drug = updated;
        updatedWithAttr.attr = updatedAttr;
        dbBackendService.updateDrugWithAttr(updatedWithAttr);
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(updated.equals(drugs.get(0).drug), "testUpdate", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
        DrugAttrDTO savedAttr = dbBackend.query(backend -> backend.getDrugAttr(drug.drugId));
        confirmNotNull(savedAttr);
        confirm(savedAttr.tekiyou.equals(updatedAttr.tekiyou));
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
    public void testUpdatePlainWithPreviousAttr() {
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        DrugAttrDTO drugAttr = new DrugAttrDTO();
        drugAttr.drugId = drug.drugId;
        drugAttr.tekiyou = "新しい摘要";
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = drugAttr;
        dbBackendService.enterDrugWithAttr(drugWithAttr);
        DrugDTO updated = DrugDTO.copy(drug);
        updated.iyakuhincode = SampleData.loxonin.iyakuhincode;
        updated.amount = 1;
        updated.usage = "頭痛時";
        updated.days = 10;
        updated.category = DrugCategory.Tonpuku.getCode();
        dbBackendService.updateDrug(updated);
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(updated.equals(drugs.get(0).drug), "testUpdate", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
        DrugAttrDTO savedAttr = dbBackend.query(backend -> backend.getDrugAttr(drug.drugId));
        confirmNotNull(savedAttr);
        confirm(savedAttr.tekiyou.equals(drugAttr.tekiyou));
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
    public void testUpdateWithAttrWithPreviousAttr() {
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        DrugAttrDTO drugAttr = new DrugAttrDTO();
        drugAttr.drugId = drug.drugId;
        drugAttr.tekiyou = "新しい摘要";
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = drugAttr;
        dbBackendService.enterDrugWithAttr(drugWithAttr);
        confirm(dbBackend.query(backend -> backend.getDrugAttr(drug.drugId)).equals(drugAttr));
        DrugDTO updated = DrugDTO.copy(drug);
        updated.iyakuhincode = SampleData.loxonin.iyakuhincode;
        updated.amount = 1;
        updated.usage = "頭痛時";
        updated.days = 10;
        updated.category = DrugCategory.Tonpuku.getCode();
        DrugAttrDTO updatedAttr = new DrugAttrDTO();
        updatedAttr.drugId = drug.drugId;
        updatedAttr.tekiyou = "変更した摘要";
        DrugWithAttrDTO updatedWithAttr = new DrugWithAttrDTO();
        updatedWithAttr.drug = updated;
        updatedWithAttr.attr = updatedAttr;
        dbBackendService.updateDrugWithAttr(updatedWithAttr);
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 1);
        confirm(updated.equals(drugs.get(0).drug), "testUpdate", () -> {
            System.out.println(drug);
            System.out.println(drugs.get(0).drug);
        });
        DrugAttrDTO savedAttr = dbBackend.query(backend -> backend.getDrugAttr(drug.drugId));
        confirmNotNull(savedAttr);
        confirm(savedAttr.tekiyou.equals(updatedAttr.tekiyou));
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
        dbBackendService.enterDrug(drug);
        dbBackendService.deleteDrug(drug.drugId);
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

    @DbTest
    public void testDeleteWithPreviousAttr(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug = new DrugDTO();
        drug.visitId = visit.visitId;
        drug.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug.amount = 3;
        drug.usage = "分３　毎食後";
        drug.days = 5;
        drug.category = DrugCategory.Naifuku.getCode();
        DrugAttrDTO drugAttr = new DrugAttrDTO();
        drugAttr.drugId = drug.drugId;
        drugAttr.tekiyou = "新しい摘要";
        enter(drug, drugAttr);
        confirm(dbBackend.query(backend -> backend.getDrugAttr(drug.drugId)).equals(drugAttr));
        dbBackendService.deleteDrug(drug.drugId);
        endExam(visit.visitId, 10);
        List<DrugFullDTO> drugs = dbBackend.query(b -> b.listDrugFull(visit.visitId));
        confirm(drugs.size() == 0);
        confirm(dbBackend.query(backend -> backend.getDrugAttr(drug.drugId)) == null);
        confirmSingleLog(logIndex, log -> log.getDrugDeleted()
                .map(drugDeleted -> drugDeleted.deleted.equals(drug))
                .orElse(false));
        confirmNoLog(logIndex, log -> log.getPharmaQueueCreated()
                .map(pharmaQueueCreated -> pharmaQueueCreated.created.visitId == visit.visitId)
                .orElse(false));
    }

    private void enter(DrugDTO drug, DrugAttrDTO attr){
        DrugWithAttrDTO drugWithAttr = new DrugWithAttrDTO();
        drugWithAttr.drug = drug;
        drugWithAttr.attr = attr;
        dbBackendService.enterDrugWithAttr(drugWithAttr);
    }

    @DbTest
    public void testMarkPrescribed(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug1 = new DrugDTO();
        drug1.visitId = visit.visitId;
        drug1.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug1.amount = 3;
        drug1.usage = "分３　毎食後";
        drug1.days = 5;
        drug1.category = DrugCategory.Naifuku.getCode();
        drug1.prescribed = 0;
        dbBackendService.enterDrug(drug1);
        DrugDTO drug2 = new DrugDTO();
        drug2.visitId = visit.visitId;
        drug2.iyakuhincode = SampleData.loxonin.iyakuhincode;
        drug2.amount = 1;
        drug2.usage = "頭痛時";
        drug2.days = 10;
        drug2.category = DrugCategory.Tonpuku.getCode();
        drug2.prescribed = 0;
        dbBackendService.enterDrug(drug2);
        endExam(visit.visitId, 640);
        int count = dbBackend.query(backend -> backend.markDrugsAsPrescribed(visit.visitId));
        confirm(count == 2);
        confirm(dbBackend.query(backend -> backend.getDrug(drug1.drugId).prescribed == 1));
        confirm(dbBackend.query(backend -> backend.getDrug(drug2.drugId).prescribed == 1));
        confirmSingleLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> {
                    DrugDTO prev = drugUpdated.prev;
                    DrugDTO updated = drugUpdated.updated;
                    return prev.drugId == drug1.drugId && prev.prescribed == 0 &&
                            updated.drugId == drug1.drugId && updated.prescribed == 1;
                })
                .orElse(false));
        confirmSingleLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> {
                    DrugDTO prev = drugUpdated.prev;
                    DrugDTO updated = drugUpdated.updated;
                    return prev.drugId == drug2.drugId && prev.prescribed == 0 &&
                            updated.drugId == drug2.drugId && updated.prescribed == 1;
                })
                .orElse(false));
    }

    @DbTest
    public void testUpdateDrugDays(){
        int logIndex = getCurrentPracticeLogIndex();
        VisitDTO visit = startExam();
        DrugDTO drug1 = new DrugDTO();
        drug1.visitId = visit.visitId;
        drug1.iyakuhincode = SampleData.calonal.iyakuhincode;
        drug1.amount = 3;
        drug1.usage = "分３　毎食後";
        drug1.days = 5;
        drug1.category = DrugCategory.Naifuku.getCode();
        drug1.prescribed = 0;
        dbBackendService.enterDrug(drug1);
        DrugDTO drug2 = new DrugDTO();
        drug2.visitId = visit.visitId;
        drug2.iyakuhincode = SampleData.loxonin.iyakuhincode;
        drug2.amount = 1;
        drug2.usage = "頭痛時";
        drug2.days = 10;
        drug2.category = DrugCategory.Tonpuku.getCode();
        drug2.prescribed = 0;
        dbBackendService.enterDrug(drug2);
        DrugDTO drug3 = new DrugDTO();
        drug3.visitId = visit.visitId;
        drug3.iyakuhincode = SampleData.loxonin.iyakuhincode;
        drug3.amount = 1;
        drug3.usage = "分１　朝食後";
        drug3.days = 10;
        drug3.category = DrugCategory.Naifuku.getCode();
        drug3.prescribed = 0;
        dbBackendService.enterDrug(drug3);
        DrugDTO drug4 = new DrugDTO();
        drug4.visitId = visit.visitId;
        drug4.iyakuhincode = SampleData.loxoninGaiyouDrug.iyakuhincode;
        drug4.amount = 51;
        drug4.usage = "１日２回、頸部に塗布";
        drug4.days = 1;
        drug4.category = DrugCategory.Tonpuku.getCode();
        drug4.prescribed = 0;
        dbBackendService.enterDrug(drug4);
        endExam(visit.visitId, 640);
        int count = dbBackend.query(backend -> backend.batchUpdateDrugDays(
                List.of(drug1.drugId, drug3.drugId, drug4.drugId),
                14
        ));
        confirm(count == 2);
        confirm(dbBackend.query(backend -> backend.getDrug(drug1.drugId).days == 14));
        confirm(dbBackend.query(backend -> backend.getDrug(drug2.drugId).days == 10));
        confirm(dbBackend.query(backend -> backend.getDrug(drug3.drugId).days == 14));
        confirm(dbBackend.query(backend -> backend.getDrug(drug4.drugId).days == 1));
        confirmSingleLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> {
                    DrugDTO prev = drugUpdated.prev;
                    DrugDTO updated = drugUpdated.updated;
                    return prev.drugId == drug1.drugId && prev.days == 5 &&
                            updated.drugId == drug1.drugId && updated.days == 14;
                })
                .orElse(false));
        confirmNoLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> drugUpdated.prev.drugId == drug2.drugId)
                .orElse(false));
        confirmSingleLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> {
                    DrugDTO prev = drugUpdated.prev;
                    DrugDTO updated = drugUpdated.updated;
                    return prev.drugId == drug3.drugId && prev.days == 10 &&
                            updated.drugId == drug3.drugId && updated.days == 14;
                })
                .orElse(false));
        confirmNoLog(logIndex, log -> log.getDrugUpdated()
                .map(drugUpdated -> drugUpdated.prev.drugId == drug4.drugId)
                .orElse(false));
    }

}
