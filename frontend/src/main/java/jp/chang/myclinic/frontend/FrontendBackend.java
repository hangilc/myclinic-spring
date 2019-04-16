package jp.chang.myclinic.frontend;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FrontendBackend implements Frontend {

    private DbBackend dbBackend;

    public FrontendBackend(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
    }

    private <T> CompletableFuture<T> query(DbBackend.QueryStatement<T> q) {
        return CompletableFuture.completedFuture(dbBackend.query(q));
    }

    private <T> CompletableFuture<T> tx(DbBackend.QueryStatement<T> q) {
        return CompletableFuture.completedFuture(dbBackend.tx(q));
    }

    private CompletableFuture<Void> txProc(DbBackend.ProcStatement p) {
        dbBackend.txProc(p);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return query(backend -> backend.getPatient(patientId));
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return query(backend -> backend.batchGetShinryouAttr(shinryouIds));
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return query(backend -> backend.batchGetDrugAttr(drugIds));
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return query(backend -> backend.batchGetShouki(visitIds));
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return tx(backend -> {
            backend.enterText(text);
            return text.textId;
        });
    }

    @Override
    public CompletableFuture<Void> updateText(TextDTO text) {
        return txProc(backend -> backend.updateText(text));
    }

    @Override
    public CompletableFuture<Void> deleteText(int textId) {
        return txProc(backend -> backend.deleteText(textId));
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return query(backend -> backend.getVisit(visitId));
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage) {
        return query(backend -> backend.listRecentVisitWithPatient(page, itemsPerPage));
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return query(be -> be.getHoken(visitId));
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        return tx(backend -> {
            backend.enterPatient(patient);
            return patient.patientId;
        });
    }

    @Override
    public CompletableFuture<Void> updatePatient(PatientDTO patient) {
        return txProc(backend -> backend.updatePatient(patient));
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword) {
        return query(backend -> backend.searchPatientByKeyword(lastNameKeyword, firstNameKeyword));
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword) {
        return query(backend -> backend.searchPatientByKeyword(keyword));
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
        return query(backend -> backend.searchPatient(text));
    }

    @Override
    public CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(int patientId, LocalDate at) {
        return query(backend -> backend.findAvailableShahokokuho(patientId, at));
    }

    @Override
    public CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(int patientId, LocalDate at) {
        return query(backend -> backend.findAvailableKoukikourei(patientId, at));
    }

    @Override
    public CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at) {
        return query(backend -> backend.findAvailableRoujin(patientId, at));
    }

    @Override
    public CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at) {
        return query(backend -> backend.findAvailableKouhi(patientId, at));
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        return tx(backend -> backend.startVisit(patientId, at));
    }

    @Override
    public CompletableFuture<Void> startExam(int visitId) {
        return txProc(backend -> backend.startExam(visitId));
    }

    @Override
    public CompletableFuture<Void> suspendExam(int visitId) {
        return txProc(backend -> backend.suspendExam(visitId));
    }

    @Override
    public CompletableFuture<Void> endExam(int visitId, int charge) {
        return txProc(backend -> backend.endExam(visitId, charge));
    }

    @Override
    public CompletableFuture<Void> enterCharge(ChargeDTO charge) {
        return txProc(backend -> backend.enterCharge(charge));
    }

    @Override
    public CompletableFuture<Void> setChargeOfVisit(int visitId, int charge) {
        return txProc(backend -> backend.setChargeOfVisit(visitId, charge));
    }

    @Override
    public CompletableFuture<ChargeDTO> getCharge(int visitId) {
        return query(backend -> backend.getCharge(visitId));
    }

    @Override
    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
        return query(backend -> backend.listPayment(visitId));
    }

    @Override
    public CompletableFuture<WqueueDTO> getWqueue(int visitId) {
        return query(backend -> backend.getWqueue(visitId));
    }

    @Override
    public CompletableFuture<Void> deleteWqueue(int visitId) {
        return txProc(backend -> backend.deleteWqueue(visitId));
    }

    @Override
    public CompletableFuture<List<WqueueDTO>> listWqueue() {
        return query(backend -> backend.listWqueue());
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
        return query(backend -> backend.listWqueueFull());
    }

    @Override
    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
        return query(backend -> backend.listAvailableHoken(patientId, visitedAt));
    }

    @Override
    public CompletableFuture<Void> updateHoken(VisitDTO visit) {
        return txProc(backend -> backend.updateHoken(visit));
    }

    @Override
    public CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId) {
        return query(backend -> backend.getDrugAttr(drugId));
    }

    @Override
    public CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr) {
        return txProc(backend -> backend.enterDrugAttr(drugAttr));
    }

    @Override
    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
        return tx(backend -> backend.setDrugTekiyou(drugId, tekiyou));
    }

    @Override
    public CompletableFuture<Void> deleteDrugTekiyou(int drugId) {
        return txProc(backend -> backend.deleteDrugTekiyou(drugId));
    }

    @Override
    public CompletableFuture<Integer> countUnprescribedDrug(int visitId) {
        return query(backend -> backend.countUnprescribedDrug(visitId));
    }

    @Override
    public CompletableFuture<Void> deleteVisitSafely(int visitId) {
        return txProc(backend -> backend.deleteVisitSafely(visitId));
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisit() {
        return query(backend -> backend.listTodaysVisit());
    }

    @Override
    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
        return query(backend -> backend.listVisitFull2(patientId, page));
    }

    @Override
    public CompletableFuture<VisitFullDTO> getVisitFull(int visitId) {
        return query(backend -> backend.getVisitFull(visitId));
    }

    @Override
    public CompletableFuture<VisitFull2DTO> getVisitFull2(int visitId) {
        return query(backend -> backend.getVisitFull2(visitId));
    }

    @Override
    public CompletableFuture<Void> updateShouki(ShoukiDTO shouki) {
        return txProc(backend -> backend.updateShouki(shouki));
    }

    @Override
    public CompletableFuture<Void> deleteShouki(int visitId) {
        return txProc(backend -> backend.deleteShouki(visitId));
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        return query(backend -> backend.getText(textId));
    }

    @Override
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        return query(backend -> backend.listText(visitId));
    }

    @Override
    public CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page) {
        return query(backend -> backend.searchText(patientId, text, page));
    }

    @Override
    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
        return query(backend -> backend.searchTextGlobally(text, page));
    }

    @Override
    public CompletableFuture<DrugDTO> getDrug(int drugId) {
        return query(backend -> backend.getDrug(drugId));
    }

    @Override
    public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
        return tx(backend -> {
            backend.enterDrug(drug);
            return drug.drugId;
        });
    }

    @Override
    public CompletableFuture<Integer> enterDrugWithAttr(DrugDTO drug, DrugAttrDTO attr) {
        return tx(backend -> {
            backend.enterDrug(drug);
            if (attr != null) {
                backend.enterDrugAttr(attr);
            }
            return drug.drugId;
        });
    }

    @Override
    public CompletableFuture<Void> updateDrugWithAttr(DrugDTO drug, DrugAttrDTO attr) {
        return txProc(backend -> {
            backend.updateDrug(drug);
            if (attr == null) {
                backend.deleteDrugAttr(drug.drugId);
            } else {
                DrugAttrDTO curr = backend.getDrugAttr(attr.drugId);
                if (curr != null) {
                    backend.updateDrugAttr(attr);
                } else {
                    backend.enterDrugAttr(attr);
                }
            }
        });
    }

    @Override
    public CompletableFuture<Void> updateDrug(DrugDTO drug) {
        return txProc(backend -> backend.updateDrug(drug));
    }

    @Override
    public CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days) {
        return txProc(backend -> backend.batchUpdateDrugDays(drugIds, days));
    }

    @Override
    public CompletableFuture<Void> deleteDrug(int drugId) {
        return txProc(backend -> backend.deleteDrug(drugId));
    }

    @Override
    public CompletableFuture<Void> deleteDrugCascading(int drugId) {
        return txProc(backend -> backend.deleteDrugCascading(drugId));
    }

    @Override
    public CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds) {
        return txProc(backend -> backend.batchDeleteDrugs(drugIds));
    }

    @Override
    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
        return query(backend -> backend.getDrugFull(drugId));
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
        return query(backend -> backend.listDrugFull(visitId));
    }

    @Override
    public CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(String text, int patientId) {
        return query(backend -> backend.listRepresentativeGaiyouDrugId(text, patientId));
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(int patientId) {
        return query(backend -> backend.searchPrevDrug(patientId));
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
        return query(backend -> backend.searchPrevDrug(text, patientId));
    }

    @Override
    public CompletableFuture<ShinryouDTO> getShinryou(int shinryouId) {
        return query(backend -> backend.getShinryou(shinryouId));
    }

    @Override
    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
        return tx(backend -> {
            backend.enterShinryou(shinryou);
            return shinryou.shinryouId;
        });
    }

    @Override
    public CompletableFuture<Void> deleteShinryou(int shinryouId) {
        return txProc(backend -> backend.deleteShinryou(shinryouId));
    }

    @Override
    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
        return query(backend -> backend.getShinryouFull(shinryouId));
    }

    @Override
    public CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList) {
        return txProc(backend -> backend.batchEnterShinryou(shinryouList));
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req) {
        return tx(backend -> backend.batchEnter(req));
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
        return query(backend -> backend.listShinryouFullByIds(shinryouIds));
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
        return query(backend -> backend.listShinryouFull(visitId));
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId) {
        return query(backend -> backend.getShinryouAttr(shinryouId));
    }

    @Override
    public CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        return txProc(backend -> backend.enterShinryouAttr(shinryouAttr));
    }

    @Override
    public CompletableFuture<Void> setShinryouAttr(int shinryouId, ShinryouAttrDTO attr) {
        return txProc(backend -> backend.setShinryouAttr(shinryouId, attr));
    }

    @Override
    public CompletableFuture<Integer> enterConduct(ConductDTO conduct) {
        return tx(backend -> {
            backend.enterConduct(conduct);
            return conduct.conductId;
        });
    }

    @Override
    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req) {
        return tx(backend -> backend.enterConductFull(req));
    }

    @Override
    public CompletableFuture<ConductDTO> getConduct(int conductId) {
        return query(backend -> backend.getConduct(conductId));
    }

    @Override
    public CompletableFuture<Void> deleteConductCascading(int conductId) {
        return txProc(backend -> backend.deleteConductCascading(conductId));
    }

    @Override
    public CompletableFuture<Void> modifyConductKind(int conductId, int conductKind) {
        return txProc(backend -> backend.modifyConductKind(conductId, conductKind));
    }

    @Override
    public CompletableFuture<List<ConductDTO>> listConduct(int visitId) {
        return query(backend -> backend.listConduct(visitId));
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
        return query(backend -> backend.listConductFullByIds(conductIds));
    }

    @Override
    public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
        return query(backend -> backend.getConductFull(conductId));
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId) {
        return query(backend -> backend.listConductFull(visitId));
    }

    @Override
    public CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel) {
        return txProc(backend -> backend.enterGazouLabel(gazouLabel));
    }

    @Override
    public CompletableFuture<Void> updateGazouLabel(GazouLabelDTO gazouLabel) {
        return txProc(backend -> backend.updateGazouLabel(gazouLabel));
    }

    @Override
    public CompletableFuture<Void> modifyGazouLabel(int conductId, String label) {
        return txProc(backend -> backend.modifyGazouLabel(conductId, label));
    }

    @Override
    public CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId) {
        return query(backend -> backend.getGazouLabel(conductId));
    }

    @Override
    public CompletableFuture<Void> deleteGazouLabel(int conductId) {
        return txProc(backend -> backend.deleteGazouLabel(conductId));
    }

    @Override
    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou) {
        return tx(backend -> {
            backend.enterConductShinryou(shinryou);
            return shinryou.conductShinryouId;
        });
    }

    @Override
    public CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId) {
        return query(backend -> backend.getConductShinryou(conductShinryouId));
    }

    @Override
    public CompletableFuture<Void> deleteConductShinryou(int conductShinryouId) {
        return txProc(backend -> backend.deleteConductShinryou(conductShinryouId));
    }

    @Override
    public CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId) {
        return query(backend -> backend.listConductShinryou(conductId));
    }

    @Override
    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
        return query(backend -> backend.getConductShinryouFull(conductShinryouId));
    }

    @Override
    public CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId) {
        return query(backend -> backend.listConductShinryouFull(conductId));
    }

    @Override
    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug) {
        return tx(backend -> {
            backend.enterConductDrug(drug);
            return drug.conductDrugId;
        });
    }

    @Override
    public CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId) {
        return query(backend -> backend.getConductDrug(conductDrugId));
    }

    @Override
    public CompletableFuture<Void> deleteConductDrug(int conductDrugId) {
        return txProc(backend -> backend.deleteConductDrug(conductDrugId));
    }

    @Override
    public CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId) {
        return query(backend -> backend.listConductDrug(conductId));
    }

    @Override
    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
        return query(backend -> backend.getConductDrugFull(conductDrugId));
    }

    @Override
    public CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId) {
        return query(backend -> backend.listConductDrugFull(conductId));
    }

    @Override
    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai) {
        return tx(backend -> {
            backend.enterConductKizai(kizai);
            return kizai.conductKizaiId;
        });
    }

    @Override
    public CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId) {
        return query(backend -> backend.getConductKizai(conductKizaiId));
    }

    @Override
    public CompletableFuture<Void> deleteConductKizai(int conductKizaiId) {
        return txProc(backend -> backend.deleteConductKizai(conductKizaiId));
    }

    @Override
    public CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId) {
        return query(backend -> backend.listConductKizai(conductId));
    }

    @Override
    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
        return query(backend -> backend.getConductKizaiFull(conductKizaiId));
    }

    @Override
    public CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId) {
        return query(backend -> backend.listConductKizaiFull(conductId));
    }

    @Override
    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
        return query(backend -> backend.getShahokokuho(shahokokuhoId));
    }

    @Override
    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
        return query(backend -> backend.getKoukikourei(koukikoureiId));
    }

    @Override
    public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
        return query(backend -> backend.getRoujin(roujinId));
    }

    @Override
    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
        return query(backend -> backend.getKouhi(kouhiId));
    }

    @Override
    public CompletableFuture<Integer> enterDisease(DiseaseDTO disease) {
        return tx(backend -> {
            backend.enterDisease(disease);
            return disease.diseaseId;
        });
    }

    @Override
    public CompletableFuture<DiseaseDTO> getDisease(int diseaseId) {
        return query(backend -> backend.getDisease(diseaseId));
    }

    @Override
    public CompletableFuture<Void> updateDisease(DiseaseDTO disease) {
        return txProc(backend -> backend.updateDisease(disease));
    }

    @Override
    public CompletableFuture<Void> deleteDisease(int diseaseId) {
        return txProc(backend -> backend.deleteDisease(diseaseId));
    }

    @Override
    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
        return query(backend -> backend.getDiseaseFull(diseaseId));
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
        return query(backend -> backend.listCurrentDiseaseFull(patientId));
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
        return query(backend -> backend.listDiseaseFull(patientId));
    }

    @Override
    public CompletableFuture<Void> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications) {
        return txProc(backend -> backend.batchUpdateDiseaseEndReason(modifications));
    }

    @Override
    public CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId) {
        return query(backend -> backend.getPharmaQueue(visitId));
    }

    @Override
    public CompletableFuture<Void> deletePharmaQueue(int visitId) {
        return txProc(backend -> backend.deletePharmaQueue(visitId));
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
        return query(backend -> backend.findShinryouMasterByName(name, at));
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at) {
        return query(backend -> backend.resolveShinryouMasterByName(nameCandidates, at));
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(List<List<String>> args, LocalDate at) {
        return query(backend -> backend.batchResolveShinryouNames(args, at));
    }

    @Override
    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, LocalDate at) {
        return query(backend -> backend.searchShinryouMaster(text, at));
    }

    @Override
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, LocalDate at) {
        return query(backend -> backend.searchIyakuhinMaster(text, at));
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at) {
        return query(backend -> backend.findKizaiMasterByName(name, at));
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at) {
        return query(backend -> backend.resolveKizaiMasterByName(nameCandidates, at));
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(List<List<String>> args, LocalDate at) {
        return query(backend -> backend.batchResolveKizaiNames(args, at));
    }

    @Override
    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at) {
        return query(backend -> backend.searchKizaiMaster(text, at));
    }

    @Override
    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at) {
        return query(backend -> backend.searchByoumeiMaster(text, at));
    }

    @Override
    public CompletableFuture<ByoumeiMasterDTO> getByoumeiMasterByName(String name, LocalDate at) {
        return query(backend -> backend.getByoumeiMasterByName(name, at));
    }

    @Override
    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(String text, LocalDate at) {
        return query(backend -> backend.searchShuushokugoMaster(text, at));
    }

    @Override
    public CompletableFuture<ShuushokugoMasterDTO> getShuushokugoMasterByName(String name) {
        return query(backend -> backend.getShuushokugoMasterByName(name));
    }

    @Override
    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
        return tx(backend -> {
            backend.enterPrescExample(prescExample);
            return prescExample.prescExampleId;
        });
    }

    @Override
    public CompletableFuture<Void> deletePrescExample(int prescExampleId) {
        return txProc(backend -> backend.deletePrescExample(prescExampleId));
    }

    @Override
    public CompletableFuture<Void> updatePrescExample(PrescExampleDTO prescExample) {
        return txProc(backend -> backend.updatePrescExample(prescExample));
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
        return query(backend -> backend.searchPrescExample(text));
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
        return query(backend -> backend.listAllPrescExample());
    }

    @Override
    public CompletableFuture<PracticeLogDTO> getLastPracticeLog() {
        return query(backend -> backend.getLastPracticeLog());
    }

    @Override
    public CompletableFuture<Integer> getLastPracticeLogId() {
        return query(backend -> backend.getLastPracticeLogId());
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId) {
        return query(backend -> backend.listPracticeLogSince(afterThisId));
    }

    @Override
    public CompletableFuture<Void> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        return txProc(backend -> backend.modifyDisease(diseaseModifyDTO));
    }

    @Override
    public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuho) {
        return tx(backend -> {
            backend.enterShahokokuho(shahokokuho);
            return shahokokuho.shahokokuhoId;
        });
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode, LocalDate at) {
        return query(backend -> backend.getIyakuhinMaster(iyakuhincode, at));
    }

    @Override
    public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
        return query(backend -> backend.deleteDuplicateShinryou(visitId));
    }

    @Override
    public CompletableFuture<List<ShinryouDTO>> listShinryou(int visitId) {
        return query(backend -> backend.listShinryou(visitId));
    }

    @Override
    public CompletableFuture<Void> finishCashier(PaymentDTO payment) {
        return txProc(backend -> backend.finishCashier(payment));
    }

    @Override
    public CompletableFuture<Void> markDrugsAsPrescribed(int visitId) {
        return txProc(backend -> backend.markDrugsAsPrescribed(visitId));
    }

    @Override
    public CompletableFuture<Void> prescDone(int visitId) {
        return txProc(backend -> backend.prescDone(visitId));
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
        return query(backend -> backend.listWqueueFullForExam());
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, LocalDate at) {
        return query(backend -> backend.getShinryouMaster(shinryoucode, at));
    }

    @Override
    public CompletableFuture<Integer> enterNewDisease(DiseaseNewDTO disease) {
        return tx(backend -> {
            backend.enterNewDisease(disease);
            return disease.disease.diseaseId;
        });
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByKey(String key, LocalDate at) {
        return query(backend -> backend.resolveShinryouMasterByKey(key, at));
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByKey(String key, LocalDate at) {
        return query(backend -> backend.resolveKizaiMasterByKey(key, at));
    }

    @Override
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        return query(backend -> backend.listDiseaseExample());
    }

    @Override
    public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
        return query(backend -> backend.getMeisai(visitId));
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnterByNames(int visitId, BatchEnterByNamesRequestDTO req) {
        return tx(backend -> backend.batchEnterByNames(visitId, req));
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> resolveStockDrug(int iyakuhincode, LocalDate at) {
        return query(backend -> backend.resolveStockDrug(iyakuhincode, at));
    }

    @Override
    public CompletableFuture<List<Integer>> copyAllConducts(int targetVisitId, int sourceVisitId) {
        return tx(backend -> backend.copyAllConducts(targetVisitId, sourceVisitId));
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode, LocalDate at) {
        return query(backend -> backend.getKizaiMaster(kizaicode, at));
    }

    @Override
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        return query(backend -> backend.getClinicInfo());
    }

    @Override
    public CompletableFuture<Void> updateDrugAttr(DrugAttrDTO drugAttr) {
        return txProc(backend -> backend.updateDrugAttr(drugAttr));
    }

    @Override
    public CompletableFuture<Void> deleteDrugAttr(int drugId) {
        return txProc(backend -> backend.deleteDrugAttr(drugId));
    }

    @Override
    public CompletableFuture<Void> deleteShinryouCascading(int shinryouId) {
        return txProc(backend -> backend.deleteShinryouCascading(shinryouId));
    }

    @Override
    public CompletableFuture<Void> batchDeleteShinryouCascading(List<Integer> shinryouIds) {
        return txProc(backend -> backend.batchDeleteShinryouCascading(shinryouIds));
    }

    @Override
    public CompletableFuture<DrugWithAttrDTO> getDrugWithAttr(int drugId) {
        return query(backend -> backend.getDrugWithAttr(drugId));
    }

    @Override
    public CompletableFuture<DrugFullWithAttrDTO> getDrugFullWithAttr(int drugId) {
        return query(backend -> backend.getDrugFullWithAttr(drugId));
    }

    @Override
    public CompletableFuture<ShinryouWithAttrDTO> getShinryouWithAttr(int shinryouId) {
        return query(backend -> backend.getShinryouWithAttr(shinryouId));
    }

    @Override
    public CompletableFuture<ShinryouFullWithAttrDTO> getShinryouFullWithAttr(int shinryouId) {
        return query(backend -> backend.getShinryouFullWithAttr(shinryouId));
    }

    @Override
    public CompletableFuture<List<ShinryouWithAttrDTO>> listShinryouWithAttr(int visitId) {
        return query(backend -> backend.listShinryouWithAttr(visitId));
    }
}
