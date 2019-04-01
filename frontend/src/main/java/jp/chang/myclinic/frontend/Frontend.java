package jp.chang.myclinic.frontend;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Frontend {

    public CompletableFuture<Integer> enterPatient(PatientDTO patient);

    public CompletableFuture<PatientDTO> getPatient(int patientId);

    public CompletableFuture<Void> updatePatient(PatientDTO patient);

    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword);

    public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword);

    public CompletableFuture<List<PatientDTO>> searchPatient(String text);

    public CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(int patientId, LocalDate at);

    public CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(int patientId, LocalDate at);

    public CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at);

    public CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at);

    public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at);

    public CompletableFuture<Void> startExam(int visitId);

    public CompletableFuture<Void> suspendExam(int visitId);

    public CompletableFuture<Void> endExam(int visitId, int charge);

    public CompletableFuture<Void> enterCharge(ChargeDTO charge);

    public CompletableFuture<Void> setChargeOfVisit(int visitId, int charge);

    public CompletableFuture<ChargeDTO> getCharge(int visitId);

    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId);

    public CompletableFuture<WqueueDTO> getWqueue(int visitId);

    public CompletableFuture<Void> deleteWqueue(int visitId);

    public CompletableFuture<List<WqueueDTO>> listWqueue();

    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull();

    public CompletableFuture<HokenDTO> getHoken(int visitId);

    public CompletableFuture<HokenDTO> getHoken(VisitDTO visit);

    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt);

    public CompletableFuture<Void> updateHoken(VisitDTO visit);

    public CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId);

    public CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr);

    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds);

    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou);

    public CompletableFuture<Void> deleteDrugTekiyou(int drugId);

    public CompletableFuture<Integer> countUnprescribedDrug(int visitId);

    public CompletableFuture<VisitDTO> getVisit(int visitId);

    public CompletableFuture<Void> deleteVisitSafely(int visitId);

    public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage);

    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisit();

    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page);

    public CompletableFuture<VisitFullDTO> getVisitFull(int visitId);

    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);

    public CompletableFuture<Void> updateShouki(ShoukiDTO shouki);

    public CompletableFuture<Void> deleteShouki(int visitId);

    public CompletableFuture<Integer> enterText(TextDTO text);

    public CompletableFuture<TextDTO> getText(int textId);

    public CompletableFuture<Void> updateText(TextDTO text);

    public CompletableFuture<Void> deleteText(int textId);

    public CompletableFuture<List<TextDTO>> listText(int visitId);

    public CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page);

    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page);

    public CompletableFuture<DrugDTO> getDrug(int drugId);

    public CompletableFuture<Integer> enterDrug(DrugDTO drug);

    public CompletableFuture<Void> updateDrug(DrugDTO drug);

    public CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days);

    public CompletableFuture<Void> deleteDrug(int drugId);

    public CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds);

    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId);

    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId);

    public CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(String text, int patientId);

    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(int patientId);

    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId);

    public CompletableFuture<ShinryouDTO> getShinryou(int shinryouId);

    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou);

    public CompletableFuture<Void> deleteShinryou(int shinryouId);

    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId);

    public CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList);

    public CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req);

    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds);

    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId);

    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);

    public CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId);

    public CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr);

    public CompletableFuture<Integer> enterConduct(ConductDTO conduct);

    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req);

    public CompletableFuture<ConductDTO> getConduct(int conductId);

    public CompletableFuture<Void> deleteConductCascading(int conductId);

    public CompletableFuture<Void> modifyConductKind(int conductId, int conductKind);

    public CompletableFuture<List<ConductDTO>> listConduct(int visitId);

    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds);

    public CompletableFuture<ConductFullDTO> getConductFull(int conductId);

    public CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId);

    public CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel);

    public CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId);

    public CompletableFuture<Void> deleteGazouLabel(int conductId);

    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou);

    public CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId);

    public CompletableFuture<Void> deleteConductShinryou(int conductShinryouId);

    public CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId);

    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId);

    public CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId);

    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug);

    public CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId);

    public CompletableFuture<Void> deleteConductDrug(int conductDrugId);

    public CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId);

    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId);

    public CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId);

    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai);

    public CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId);

    public CompletableFuture<Void> deleteConductKizai(int conductKizaiId);

    public CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId);

    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId);

    public CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId);

    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId);

    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId);

    public CompletableFuture<RoujinDTO> getRoujin(int roujinId);

    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId);

    public CompletableFuture<Integer> enterDisease(DiseaseDTO disease);

    public CompletableFuture<DiseaseDTO> getDisease(int diseaseId);

    public CompletableFuture<Void> updateDisease(DiseaseDTO disease);

    public CompletableFuture<Void> deleteDisease(int diseaseId);

    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId);

    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId);

    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId);

    public CompletableFuture<Void> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications);

    public CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId);

    public CompletableFuture<Void> deletePharmaQueue(int visitId);

    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at);

    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at);

    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(List<List<String>> args, LocalDate at);

    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, LocalDate at);

    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, LocalDate at);

    public CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at);

    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at);

    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(List<List<String>> args, LocalDate at);

    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at);

    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at);

    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(String text, LocalDate at);

    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample);

    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text);

    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample();

    public CompletableFuture<PracticeLogDTO> getLastPracticeLog();

    public CompletableFuture<Integer> getLastPracticeLogId();

    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId);
}
