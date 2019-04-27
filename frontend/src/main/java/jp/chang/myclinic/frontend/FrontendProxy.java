package jp.chang.myclinic.frontend;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FrontendProxy implements Frontend {

    private Frontend delegate;

    public FrontendProxy(Frontend delegate) {
        this.delegate = delegate;
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        return delegate.enterPatient(patient);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return delegate.getPatient(patientId);
    }

    @Override
    public CompletableFuture<Void> updatePatient(PatientDTO patient) {
        return delegate.updatePatient(patient);
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword2(String lastNameKeyword, String firstNameKeyword) {
        return delegate.searchPatientByKeyword2(lastNameKeyword, firstNameKeyword);
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword) {
        return delegate.searchPatientByKeyword(keyword);
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
        return delegate.searchPatient(text);
    }

    @Override
    public CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(int patientId, LocalDate at) {
        return delegate.findAvailableShahokokuho(patientId, at);
    }

    @Override
    public CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(int patientId, LocalDate at) {
        return delegate.findAvailableKoukikourei(patientId, at);
    }

    @Override
    public CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at) {
        return delegate.findAvailableRoujin(patientId, at);
    }

    @Override
    public CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at) {
        return delegate.findAvailableKouhi(patientId, at);
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        return delegate.startVisit(patientId, at);
    }

    @Override
    public CompletableFuture<Void> startExam(int visitId) {
        return delegate.startExam(visitId);
    }

    @Override
    public CompletableFuture<Void> suspendExam(int visitId) {
        return delegate.suspendExam(visitId);
    }

    @Override
    public CompletableFuture<Void> endExam(int visitId, int charge) {
        return delegate.endExam(visitId, charge);
    }

    @Override
    public CompletableFuture<Void> enterCharge(ChargeDTO charge) {
        return delegate.enterCharge(charge);
    }

    @Override
    public CompletableFuture<Void> setChargeOfVisit(int visitId, int charge) {
        return delegate.setChargeOfVisit(visitId, charge);
    }

    @Override
    public CompletableFuture<ChargeDTO> getCharge(int visitId) {
        return delegate.getCharge(visitId);
    }

    @Override
    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
        return delegate.listPayment(visitId);
    }

    @Override
    public CompletableFuture<WqueueDTO> getWqueue(int visitId) {
        return delegate.getWqueue(visitId);
    }

    @Override
    public CompletableFuture<Void> deleteWqueue(int visitId) {
        return delegate.deleteWqueue(visitId);
    }

    @Override
    public CompletableFuture<List<WqueueDTO>> listWqueue() {
        return delegate.listWqueue();
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
        return delegate.listWqueueFull();
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return delegate.getHoken(visitId);
    }

    @Override
    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
        return delegate.listAvailableHoken(patientId, visitedAt);
    }

    @Override
    public CompletableFuture<Void> updateHoken(VisitDTO visit) {
        return delegate.updateHoken(visit);
    }

    @Override
    public CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId) {
        return delegate.getDrugAttr(drugId);
    }

    @Override
    public CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr) {
        return delegate.enterDrugAttr(drugAttr);
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return delegate.batchGetDrugAttr(drugIds);
    }

    @Override
    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
        return delegate.setDrugTekiyou(drugId, tekiyou);
    }

    @Override
    public CompletableFuture<Void> deleteDrugTekiyou(int drugId) {
        return delegate.deleteDrugTekiyou(drugId);
    }

    @Override
    public CompletableFuture<Integer> countUnprescribedDrug(int visitId) {
        return delegate.countUnprescribedDrug(visitId);
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return delegate.getVisit(visitId);
    }

    @Override
    public CompletableFuture<Void> deleteVisitSafely(int visitId) {
        return delegate.deleteVisitSafely(visitId);
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage) {
        return delegate.listRecentVisitWithPatient(page, itemsPerPage);
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisit() {
        return delegate.listTodaysVisit();
    }

    @Override
    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
        return delegate.listVisitFull2(patientId, page);
    }

    @Override
    public CompletableFuture<VisitFullDTO> getVisitFull(int visitId) {
        return delegate.getVisitFull(visitId);
    }

    @Override
    public CompletableFuture<VisitFull2DTO> getVisitFull2(int visitId) {
        return delegate.getVisitFull2(visitId);
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return delegate.batchGetShouki(visitIds);
    }

    @Override
    public CompletableFuture<Void> updateShouki(ShoukiDTO shouki) {
        return delegate.updateShouki(shouki);
    }

    @Override
    public CompletableFuture<Void> deleteShouki(int visitId) {
        return delegate.deleteShouki(visitId);
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        return delegate.enterText(text);
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        return delegate.getText(textId);
    }

    @Override
    public CompletableFuture<Void> updateText(TextDTO text) {
        return delegate.updateText(text);
    }

    @Override
    public CompletableFuture<Void> deleteText(int textId) {
        return delegate.deleteText(textId);
    }

    @Override
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        return delegate.listText(visitId);
    }

    @Override
    public CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page) {
        return delegate.searchText(patientId, text, page);
    }

    @Override
    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
        return delegate.searchTextGlobally(text, page);
    }

    @Override
    public CompletableFuture<DrugDTO> getDrug(int drugId) {
        return delegate.getDrug(drugId);
    }

    @Override
    public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
        return delegate.enterDrug(drug);
    }

    @Override
    public CompletableFuture<Void> updateDrug(DrugDTO drug) {
        return delegate.updateDrug(drug);
    }

    @Override
    public CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days) {
        return delegate.batchUpdateDrugDays(drugIds, days);
    }

    @Override
    public CompletableFuture<Void> deleteDrug(int drugId) {
        return delegate.deleteDrug(drugId);
    }

    @Override
    public CompletableFuture<Void> deleteDrugCascading(int drugId) {
        return delegate.deleteDrugCascading(drugId);
    }

    @Override
    public CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds) {
        return delegate.batchDeleteDrugs(drugIds);
    }

    @Override
    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
        return delegate.getDrugFull(drugId);
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
        return delegate.listDrugFull(visitId);
    }

    @Override
    public CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(String text, int patientId) {
        return delegate.listRepresentativeGaiyouDrugId(text, patientId);
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listPrevDrugByPatient(int patientId) {
        return delegate.listPrevDrugByPatient(patientId);
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
        return delegate.searchPrevDrug(text, patientId);
    }

    @Override
    public CompletableFuture<ShinryouDTO> getShinryou(int shinryouId) {
        return delegate.getShinryou(shinryouId);
    }

    @Override
    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
        return delegate.enterShinryou(shinryou);
    }

    @Override
    public CompletableFuture<Void> deleteShinryou(int shinryouId) {
        return delegate.deleteShinryou(shinryouId);
    }

    @Override
    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
        return delegate.getShinryouFull(shinryouId);
    }

    @Override
    public CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList) {
        return delegate.batchEnterShinryou(shinryouList);
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req) {
        return delegate.batchEnter(req);
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
        return delegate.listShinryouFullByIds(shinryouIds);
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
        return delegate.listShinryouFull(visitId);
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return delegate.batchGetShinryouAttr(shinryouIds);
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId) {
        return delegate.getShinryouAttr(shinryouId);
    }

    @Override
    public CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        return delegate.enterShinryouAttr(shinryouAttr);
    }

    @Override
    public CompletableFuture<Void> setShinryouAttr(int shinryouId, ShinryouAttrDTO attr) {
        return delegate.setShinryouAttr(shinryouId, attr);
    }

    @Override
    public CompletableFuture<Integer> enterConduct(ConductDTO conduct) {
        return delegate.enterConduct(conduct);
    }

    @Override
    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req) {
        return delegate.enterConductFull(req);
    }

    @Override
    public CompletableFuture<ConductDTO> getConduct(int conductId) {
        return delegate.getConduct(conductId);
    }

    @Override
    public CompletableFuture<Void> deleteConductCascading(int conductId) {
        return delegate.deleteConductCascading(conductId);
    }

    @Override
    public CompletableFuture<Void> modifyConductKind(int conductId, int conductKind) {
        return delegate.modifyConductKind(conductId, conductKind);
    }

    @Override
    public CompletableFuture<List<ConductDTO>> listConduct(int visitId) {
        return delegate.listConduct(visitId);
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
        return delegate.listConductFullByIds(conductIds);
    }

    @Override
    public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
        return delegate.getConductFull(conductId);
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId) {
        return delegate.listConductFull(visitId);
    }

    @Override
    public CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel) {
        return delegate.enterGazouLabel(gazouLabel);
    }

    @Override
    public CompletableFuture<Void> updateGazouLabel(GazouLabelDTO gazouLabel) {
        return delegate.updateGazouLabel(gazouLabel);
    }

    @Override
    public CompletableFuture<Void> modifyGazouLabel(int conductId, String label) {
        return delegate.modifyGazouLabel(conductId, label);
    }

    @Override
    public CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId) {
        return delegate.getGazouLabel(conductId);
    }

    @Override
    public CompletableFuture<Void> deleteGazouLabel(int conductId) {
        return delegate.deleteGazouLabel(conductId);
    }

    @Override
    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou) {
        return delegate.enterConductShinryou(shinryou);
    }

    @Override
    public CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId) {
        return delegate.getConductShinryou(conductShinryouId);
    }

    @Override
    public CompletableFuture<Void> deleteConductShinryou(int conductShinryouId) {
        return delegate.deleteConductShinryou(conductShinryouId);
    }

    @Override
    public CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId) {
        return delegate.listConductShinryou(conductId);
    }

    @Override
    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
        return delegate.getConductShinryouFull(conductShinryouId);
    }

    @Override
    public CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId) {
        return delegate.listConductShinryouFull(conductId);
    }

    @Override
    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug) {
        return delegate.enterConductDrug(drug);
    }

    @Override
    public CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId) {
        return delegate.getConductDrug(conductDrugId);
    }

    @Override
    public CompletableFuture<Void> deleteConductDrug(int conductDrugId) {
        return delegate.deleteConductDrug(conductDrugId);
    }

    @Override
    public CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId) {
        return delegate.listConductDrug(conductId);
    }

    @Override
    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
        return delegate.getConductDrugFull(conductDrugId);
    }

    @Override
    public CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId) {
        return delegate.listConductDrugFull(conductId);
    }

    @Override
    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai) {
        return delegate.enterConductKizai(kizai);
    }

    @Override
    public CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId) {
        return delegate.getConductKizai(conductKizaiId);
    }

    @Override
    public CompletableFuture<Void> deleteConductKizai(int conductKizaiId) {
        return delegate.deleteConductKizai(conductKizaiId);
    }

    @Override
    public CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId) {
        return delegate.listConductKizai(conductId);
    }

    @Override
    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
        return delegate.getConductKizaiFull(conductKizaiId);
    }

    @Override
    public CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId) {
        return delegate.listConductKizaiFull(conductId);
    }

    @Override
    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
        return delegate.getShahokokuho(shahokokuhoId);
    }

    @Override
    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
        return delegate.getKoukikourei(koukikoureiId);
    }

    @Override
    public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
        return delegate.getRoujin(roujinId);
    }

    @Override
    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
        return delegate.getKouhi(kouhiId);
    }

    @Override
    public CompletableFuture<Integer> enterDisease(DiseaseDTO disease) {
        return delegate.enterDisease(disease);
    }

    @Override
    public CompletableFuture<DiseaseDTO> getDisease(int diseaseId) {
        return delegate.getDisease(diseaseId);
    }

    @Override
    public CompletableFuture<Void> updateDisease(DiseaseDTO disease) {
        return delegate.updateDisease(disease);
    }

    @Override
    public CompletableFuture<Void> deleteDisease(int diseaseId) {
        return delegate.deleteDisease(diseaseId);
    }

    @Override
    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
        return delegate.getDiseaseFull(diseaseId);
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
        return delegate.listCurrentDiseaseFull(patientId);
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
        return delegate.listDiseaseFull(patientId);
    }

    @Override
    public CompletableFuture<Void> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications) {
        return delegate.batchUpdateDiseaseEndReason(modifications);
    }

    @Override
    public CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId) {
        return delegate.getPharmaQueue(visitId);
    }

    @Override
    public CompletableFuture<Void> deletePharmaQueue(int visitId) {
        return delegate.deletePharmaQueue(visitId);
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
        return delegate.findShinryouMasterByName(name, at);
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at) {
        return delegate.resolveShinryouMasterByName(nameCandidates, at);
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(List<List<String>> args, LocalDate at) {
        return delegate.batchResolveShinryouNames(args, at);
    }

    @Override
    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, LocalDate at) {
        return delegate.searchShinryouMaster(text, at);
    }

    @Override
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, LocalDate at) {
        return delegate.searchIyakuhinMaster(text, at);
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at) {
        return delegate.findKizaiMasterByName(name, at);
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at) {
        return delegate.resolveKizaiMasterByName(nameCandidates, at);
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(List<List<String>> args, LocalDate at) {
        return delegate.batchResolveKizaiNames(args, at);
    }

    @Override
    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at) {
        return delegate.searchKizaiMaster(text, at);
    }

    @Override
    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at) {
        return delegate.searchByoumeiMaster(text, at);
    }

    @Override
    public CompletableFuture<ByoumeiMasterDTO> getByoumeiMasterByName(String name, LocalDate at) {
        return delegate.getByoumeiMasterByName(name, at);
    }

    @Override
    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(String text, LocalDate at) {
        return delegate.searchShuushokugoMaster(text, at);
    }

    @Override
    public CompletableFuture<ShuushokugoMasterDTO> getShuushokugoMasterByName(String name) {
        return delegate.getShuushokugoMasterByName(name);
    }

    @Override
    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
        return delegate.enterPrescExample(prescExample);
    }

    @Override
    public CompletableFuture<Void> deletePrescExample(int prescExampleId) {
        return delegate.deletePrescExample(prescExampleId);
    }

    @Override
    public CompletableFuture<Void> updatePrescExample(PrescExampleDTO prescExample) {
        return delegate.updatePrescExample(prescExample);
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
        return delegate.searchPrescExample(text);
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
        return delegate.listAllPrescExample();
    }

    @Override
    public CompletableFuture<PracticeLogDTO> getLastPracticeLog() {
        return delegate.getLastPracticeLog();
    }

    @Override
    public CompletableFuture<Integer> getLastPracticeLogId() {
        return delegate.getLastPracticeLogId();
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId) {
        return delegate.listPracticeLogSince(afterThisId);
    }

    @Override
    public CompletableFuture<Void> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        return delegate.modifyDisease(diseaseModifyDTO);
    }

    @Override
    public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuho) {
        return delegate.enterShahokokuho(shahokokuho);
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode, LocalDate at) {
        return delegate.getIyakuhinMaster(iyakuhincode, at);
    }

    @Override
    public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
        return delegate.deleteDuplicateShinryou(visitId);
    }

    @Override
    public CompletableFuture<List<ShinryouDTO>> listShinryou(int visitId) {
        return delegate.listShinryou(visitId);
    }

    @Override
    public CompletableFuture<Void> finishCashier(PaymentDTO payment) {
        return delegate.finishCashier(payment);
    }

    @Override
    public CompletableFuture<Void> markDrugsAsPrescribed(int visitId) {
        return delegate.markDrugsAsPrescribed(visitId);
    }

    @Override
    public CompletableFuture<Void> prescDone(int visitId) {
        return delegate.prescDone(visitId);
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
        return delegate.listWqueueFullForExam();
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, LocalDate at) {
        return delegate.getShinryouMaster(shinryoucode, at);
    }

    @Override
    public CompletableFuture<Integer> enterNewDisease(DiseaseNewDTO disease) {
        return delegate.enterNewDisease(disease);
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByKey(String key, LocalDate at) {
        return delegate.resolveShinryouMasterByKey(key, at);
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByKey(String key, LocalDate at) {
        return delegate.resolveKizaiMasterByKey(key, at);
    }

    @Override
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        return delegate.listDiseaseExample();
    }

    @Override
    public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
        return delegate.getMeisai(visitId);
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnterByNames(int visitId, BatchEnterByNamesRequestDTO req) {
        return delegate.batchEnterByNames(visitId, req);
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> resolveStockDrug(int iyakuhincode, LocalDate at) {
        return delegate.resolveStockDrug(iyakuhincode, at);
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode, LocalDate at) {
        return delegate.getKizaiMaster(kizaicode, at);
    }

    @Override
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        return delegate.getClinicInfo();
    }

    @Override
    public CompletableFuture<Void> updateDrugAttr(DrugAttrDTO drugAttr) {
        return delegate.updateDrugAttr(drugAttr);
    }

    @Override
    public CompletableFuture<Void> deleteDrugAttr(int drugId) {
        return delegate.deleteDrugAttr(drugId);
    }

    @Override
    public CompletableFuture<Void> deleteShinryouCascading(int shinryouId) {
        return delegate.deleteShinryouCascading(shinryouId);
    }

    @Override
    public CompletableFuture<Void> batchDeleteShinryouCascading(List<Integer> shinryouIds) {
        return delegate.batchDeleteShinryouCascading(shinryouIds);
    }

    @Override
    public CompletableFuture<DrugWithAttrDTO> getDrugWithAttr(int drugId) {
        return delegate.getDrugWithAttr(drugId);
    }

    @Override
    public CompletableFuture<DrugFullWithAttrDTO> getDrugFullWithAttr(int drugId) {
        return delegate.getDrugFullWithAttr(drugId);
    }

    @Override
    public CompletableFuture<ShinryouWithAttrDTO> getShinryouWithAttr(int shinryouId) {
        return delegate.getShinryouWithAttr(shinryouId);
    }

    @Override
    public CompletableFuture<ShinryouFullWithAttrDTO> getShinryouFullWithAttr(int shinryouId) {
        return delegate.getShinryouFullWithAttr(shinryouId);
    }

    @Override
    public CompletableFuture<List<ShinryouWithAttrDTO>> listShinryouWithAttr(int visitId) {
        return delegate.listShinryouWithAttr(visitId);
    }

    @Override
    public CompletableFuture<List<DrugWithAttrDTO>> listDrugWithAttr(int visitId) {
        return delegate.listDrugWithAttr(visitId);
    }

    @Override
    public CompletableFuture<List<ShinryouFullWithAttrDTO>> listShinryouFullWithAttrByIds(List<Integer> shinryouIds) {
        return delegate.listShinryouFullWithAttrByIds(shinryouIds);
    }

    @Override
    public CompletableFuture<List<ShinryouFullWithAttrDTO>> listShinryouFullWithAttr(int visitId) {
        return delegate.listShinryouFullWithAttr(visitId);
    }

    @Override
    public CompletableFuture<List<ResolvedStockDrugDTO>> batchResolveStockDrug(List<Integer> iyakuhincodes, LocalDate at) {
        return delegate.batchResolveStockDrug(iyakuhincodes, at);
    }

    @Override
    public CompletableFuture<ShinryouDTO> enterShinryouByName(int visitId, String name) {
        return delegate.enterShinryouByName(visitId, name);
    }

}
