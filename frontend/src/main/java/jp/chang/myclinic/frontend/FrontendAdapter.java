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

public class FrontendAdapter implements Frontend {

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updatePatient(PatientDTO patient) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> startExam(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> suspendExam(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> endExam(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> enterCharge(ChargeDTO charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> setChargeOfVisit(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ChargeDTO> getCharge(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<WqueueDTO> getWqueue(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteWqueue(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<WqueueDTO>> listWqueue() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(VisitDTO visit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateHoken(VisitDTO visit) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteDrugTekiyou(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> countUnprescribedDrug(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteVisitSafely(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisit() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitFullDTO> getVisitFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateShouki(ShoukiDTO shouki) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteShouki(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateText(TextDTO text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<TextDTO>> listText(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugDTO> getDrug(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateDrug(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteDrug(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(String text, int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouDTO> getShinryou(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteShinryou(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConduct(ConductDTO conduct) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductDTO> getConduct(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteConductCascading(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> modifyConductKind(int conductId, int conductKind) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductDTO>> listConduct(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateGazouLabel(GazouLabelDTO gazouLabel) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> modifyGazouLabel(int conductId, String label) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteGazouLabel(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteConductShinryou(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteConductDrug(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteConductKizai(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterDisease(DiseaseDTO disease) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DiseaseDTO> getDisease(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updateDisease(DiseaseDTO disease) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteDisease(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deletePharmaQueue(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(List<List<String>> args, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(List<List<String>> args, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ByoumeiMasterDTO> getByoumeiMasterByName(String name, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(String text, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShuushokugoMasterDTO> getShuushokugoMasterByName(String name) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deletePrescExample(int prescExampleId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> updatePrescExample(PrescExampleDTO prescExample) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PracticeLogDTO> getLastPracticeLog() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> getLastPracticeLogId() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> deleteShinryouTekiyou(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> setShinryouTekiyou(int shinryouId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuho) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouDTO>> listShinryou(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> finishCashier(PaymentDTO payment) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> markDrugsAsPrescribed(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Void> prescDone(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterNewDisease(DiseaseNewDTO disease) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByKey(String key, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByKey(String key, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnterByNames(int visitId, BatchEnterByNamesRequestDTO req) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> resolveStockDrug(int iyakuhincode, LocalDate at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> copyAllConducts(int targetVisitId, int sourceVisitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode, LocalDate at) {
        throw new RuntimeException("not implemented");
    }
}
