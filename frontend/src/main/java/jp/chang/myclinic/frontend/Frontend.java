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

    CompletableFuture<Integer> enterPatient(PatientDTO patient);

    CompletableFuture<PatientDTO> getPatient(int patientId);

    CompletableFuture<Void> updatePatient(PatientDTO patient);

    CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String lastNameKeyword, String firstNameKeyword);

    CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword);

    CompletableFuture<List<PatientDTO>> searchPatient(String text);

    CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(int patientId, LocalDate at);

    CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(int patientId, LocalDate at);

    CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at);

    CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at);

    CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at);

    CompletableFuture<Void> startExam(int visitId);

    CompletableFuture<Void> suspendExam(int visitId);

    CompletableFuture<Void> endExam(int visitId, int charge);

    CompletableFuture<Void> enterCharge(ChargeDTO charge);

    CompletableFuture<Void> setChargeOfVisit(int visitId, int charge);

    CompletableFuture<ChargeDTO> getCharge(int visitId);

    CompletableFuture<List<PaymentDTO>> listPayment(int visitId);

    CompletableFuture<WqueueDTO> getWqueue(int visitId);

    CompletableFuture<Void> deleteWqueue(int visitId);

    CompletableFuture<List<WqueueDTO>> listWqueue();

    CompletableFuture<List<WqueueFullDTO>> listWqueueFull();

    CompletableFuture<HokenDTO> getHoken(int visitId);

    CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt);

    CompletableFuture<Void> updateHoken(VisitDTO visit);

    CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId);

    CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr);

    CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds);

    CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou);

    CompletableFuture<Void> deleteDrugTekiyou(int drugId);

    CompletableFuture<Integer> countUnprescribedDrug(int visitId);

    CompletableFuture<VisitDTO> getVisit(int visitId);

    CompletableFuture<Void> deleteVisitSafely(int visitId);

    CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(int page, int itemsPerPage);

    CompletableFuture<List<VisitPatientDTO>> listTodaysVisit();

    CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page);

    CompletableFuture<VisitFullDTO> getVisitFull(int visitId);

    CompletableFuture<VisitFull2DTO> getVisitFull2(int visitId);

    CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds);

    CompletableFuture<Void> updateShouki(ShoukiDTO shouki);

    CompletableFuture<Void> deleteShouki(int visitId);

    CompletableFuture<Integer> enterText(TextDTO text);

    CompletableFuture<TextDTO> getText(int textId);

    CompletableFuture<Void> updateText(TextDTO text);

    CompletableFuture<Void> deleteText(int textId);

    CompletableFuture<List<TextDTO>> listText(int visitId);

    CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page);

    CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page);

    CompletableFuture<DrugDTO> getDrug(int drugId);

    CompletableFuture<Integer> enterDrug(DrugDTO drug);

    CompletableFuture<Integer> enterDrugWithAttr(DrugDTO drug, DrugAttrDTO attr);

    CompletableFuture<Void> updateDrug(DrugDTO drug);

    CompletableFuture<Void> updateDrugWithAttr(DrugDTO drug, DrugAttrDTO attr);

    CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days);

    CompletableFuture<Void> deleteDrug(int drugId);

    CompletableFuture<Void> deleteDrugCascading(int drugId);

    CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds);

    CompletableFuture<DrugFullDTO> getDrugFull(int drugId);

    CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId);

    CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(String text, int patientId);

    CompletableFuture<List<DrugFullDTO>> searchPrevDrug(int patientId);

    CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId);

    CompletableFuture<ShinryouDTO> getShinryou(int shinryouId);

    CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou);

    CompletableFuture<Void> deleteShinryou(int shinryouId);

    CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId);

    CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList);

    CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req);

    CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds);

    CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId);

    CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds);

    CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId);

    CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr);

    CompletableFuture<Void> setShinryouAttr(int shinryouId, ShinryouAttrDTO attr);

    CompletableFuture<Integer> enterConduct(ConductDTO conduct);

    CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req);

    CompletableFuture<ConductDTO> getConduct(int conductId);

    CompletableFuture<Void> deleteConductCascading(int conductId);

    CompletableFuture<Void> modifyConductKind(int conductId, int conductKind);

    CompletableFuture<List<ConductDTO>> listConduct(int visitId);

    CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds);

    CompletableFuture<ConductFullDTO> getConductFull(int conductId);

    CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId);

    CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel);

    CompletableFuture<Void> updateGazouLabel(GazouLabelDTO gazouLabel);

    CompletableFuture<Void> modifyGazouLabel(int conductId, String label);

    CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId);

    CompletableFuture<Void> deleteGazouLabel(int conductId);

    CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou);

    CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId);

    CompletableFuture<Void> deleteConductShinryou(int conductShinryouId);

    CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId);

    CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId);

    CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId);

    CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug);

    CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId);

    CompletableFuture<Void> deleteConductDrug(int conductDrugId);

    CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId);

    CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId);

    CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId);

    CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai);

    CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId);

    CompletableFuture<Void> deleteConductKizai(int conductKizaiId);

    CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId);

    CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId);

    CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId);

    CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId);

    CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId);

    CompletableFuture<RoujinDTO> getRoujin(int roujinId);

    CompletableFuture<KouhiDTO> getKouhi(int kouhiId);

    CompletableFuture<Integer> enterDisease(DiseaseDTO disease);

    CompletableFuture<DiseaseDTO> getDisease(int diseaseId);

    CompletableFuture<Void> updateDisease(DiseaseDTO disease);

    CompletableFuture<Void> deleteDisease(int diseaseId);

    CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId);

    CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId);

    CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId);

    CompletableFuture<Void> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications);

    CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId);

    CompletableFuture<Void> deletePharmaQueue(int visitId);

    CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at);

    CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(List<String> nameCandidates, LocalDate at);

    CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(List<List<String>> args, LocalDate at);

    CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, LocalDate at);

    CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, LocalDate at);

    CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at);

    CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(List<String> nameCandidates, LocalDate at);

    CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(List<List<String>> args, LocalDate at);

    CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at);

    CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at);

    CompletableFuture<ByoumeiMasterDTO> getByoumeiMasterByName(String name, LocalDate at);

    CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(String text, LocalDate at);

    CompletableFuture<ShuushokugoMasterDTO> getShuushokugoMasterByName(String name);

    CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample);

    CompletableFuture<Void> deletePrescExample(int prescExampleId);

    CompletableFuture<Void> updatePrescExample(PrescExampleDTO prescExample);

    CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text);

    CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample();

    CompletableFuture<PracticeLogDTO> getLastPracticeLog();

    CompletableFuture<Integer> getLastPracticeLogId();

    CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId);

    CompletableFuture<Void> modifyDisease(DiseaseModifyDTO diseaseModifyDTO);

    CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuho);

    CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode, LocalDate at);

    CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId);

    CompletableFuture<List<ShinryouDTO>> listShinryou(int visitId);

    CompletableFuture<Void> finishCashier(PaymentDTO payment);

    CompletableFuture<Void> markDrugsAsPrescribed(int visitId);

    CompletableFuture<Void> prescDone(int visitId);

    CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam();

    CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, LocalDate at);

    CompletableFuture<Integer> enterNewDisease(DiseaseNewDTO disease);

    CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByKey(String key, LocalDate at);

    CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByKey(String key, LocalDate at);

    CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample();

    CompletableFuture<MeisaiDTO> getMeisai(int visitId);

    CompletableFuture<BatchEnterResultDTO> batchEnterByNames(int visitId, BatchEnterByNamesRequestDTO req);

    CompletableFuture<IyakuhinMasterDTO> resolveStockDrug(int iyakuhincode, LocalDate at);

    CompletableFuture<List<Integer>> copyAllConducts(int targetVisitId, int sourceVisitId);

    CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode, LocalDate at);

    CompletableFuture<ClinicInfoDTO> getClinicInfo();

    CompletableFuture<Void> updateDrugAttr(DrugAttrDTO drugAttr);

    CompletableFuture<Void> deleteDrugAttr(int drugId);

    CompletableFuture<Void> deleteShinryouCascading(int shinryouId);
}
