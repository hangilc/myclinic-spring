package jp.chang.myclinic.dbgateway;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DbGatewayInterface {
    List<WqueueFullDTO> listWqueueFull();

    WqueueFullDTO getWqueueFull(int visitId);

    List<WqueueFullDTO> listWqueueFullByStates(Set<WqueueWaitState> states);

    List<WqueueDTO> listWqueueByStates(Set<WqueueWaitState> states, Sort sort);

    List<VisitPatientDTO> listTodaysVisits();

    VisitFull2PatientPageDTO pageVisitsWithPatientAt(LocalDate date, int page);

    List<VisitFull2PatientDTO> listVisitFull2PatientOfToday();

    void enterWqueue(WqueueDTO wqueueDTO);

    Optional<WqueueDTO> findWqueue(int visitId);

    void deleteWqueue(WqueueDTO wqueueDTO);

    void startExam(int visitId);

    void suspendExam(int visitId);

    void endExam(int visitId, int charge);

    PatientDTO getPatient(int patientId);

    Optional<PatientDTO> findPatient(int patientId);

    int enterPatient(PatientDTO patientDTO);

    void updatePatient(PatientDTO patientDTO);

    List<PatientDTO> searchPatientByLastName(String text);

    List<PatientDTO> searchPatientByFirstName(String text);

    List<PatientDTO> searchPatientByLastNameYomi(String text);

    List<PatientDTO> searchPatientByFirstNameYomi(String text);

    List<PatientDTO> searchPatientByName(String lastName, String firstName);

    List<PatientDTO> searchPatientByYomi(String lastNameYomi, String firstNameYomi);

    List<PatientDTO> searchPatient(String text);

    List<PatientDTO> searchPatient(String textLastName, String textFirstName);

    List<PatientDTO> listRecentlyRegisteredPatients(int n);

    int enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO);

    void updateShahokokuho(ShahokokuhoDTO shahokokuhoDTO);

    void deleteShahokokuho(int shahokokuhoId);

    List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate at);

    ShahokokuhoDTO getShahokokuho(int shahokokuhoId);

    int enterKoukikourei(KoukikoureiDTO koukikoureiDTO);

    void updateKoukikourei(KoukikoureiDTO koukikoureiDTO);

    void deleteKoukikourei(int koukikoureiId);

    List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate at);

    int enterRoujin(RoujinDTO roujinDTO);

    KoukikoureiDTO getKoukikourei(int koukikoureiId);

    void deleteRoujin(int roujinId);

    List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate at);

    RoujinDTO getRoujin(int roujinId);

    int enterKouhi(KouhiDTO kouhiDTO);

    void updateKouhi(KouhiDTO kouhiDTO);

    void deleteKouhi(int kouhiId);

    List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate at);

    KouhiDTO getKouhi(int kouhiId);

    HokenListDTO findHokenByPatient(int patientId);

    HokenDTO listAvailableHoken(int patientId, String at);

    void enterCharge(ChargeDTO chargeDTO);

    ChargeDTO getCharge(int visitId);

    Optional<ChargeDTO> findCharge(int visitId);

    void setChargeOfVisit(int visitId, int charge);

    void enterPayment(PaymentDTO paymentDTO);

    List<PaymentDTO> listPayment(int visitId);

    VisitDTO getVisit(int visitId);

    int enterVisit(VisitDTO visitDTO);

    void updateVisit(VisitDTO visitDTO);

    List<Integer> listVisitIds();

    List<Integer> listVisitIdsForPatient(int patientId);

    List<VisitIdVisitedAtDTO> listVisitIdVisitedAtForPatient(int patientId);

    List<VisitPatientDTO> listVisitWithPatient(int page, int itemsPerPage);

    ShinryouFullDTO getShinryouFull(int shinryouId);

    ShinryouDTO getShinryou(int shinryouId);

    void batchDeleteShinryou(List<Integer> shinryouIds);

    List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds);

    List<ShinryouFullDTO> listShinryouFull(int visitId);

    ShinryouDTO enterShinryou(ShinryouDTO shinryouDTO);

    void updateShinryou(ShinryouDTO shinryouDTO);

    void deleteShinryou(int shinryouId);

    List<Integer> deleteDuplicateShinryou(int visitId);

    Optional<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at);

    Optional<ShinryouMasterDTO> findShinryouMasterByShinryoucode(int shinryoucode, LocalDate at);

    DrugFullDTO getDrugFull(int drugId);

    List<DrugDTO> listDrug(int visitId);

    List<DrugFullDTO> listDrugFull(int visitId);

    int enterDrug(DrugDTO drugDTO);

    DrugDTO getDrug(int drugId);

    void deleteDrug(int drugId);

    void updateDrug(DrugDTO drugDTO);

    void markDrugsAsPrescribedForVisit(int visitId);

    List<DrugFullDTO> searchPrevDrug(int patientId);

    List<DrugFullDTO> searchPrevDrug(int patientId, String text);

    void batchUpdateDrugDays(List<Integer> drugIds, int days);

    TextDTO getText(int textId);

    int enterText(TextDTO textDTO);

    void updateText(TextDTO textDTO);

    void deleteText(int textId);

    TextVisitPageDTO searchText(int patientId, String text, int page);

    List<ShinryouMasterDTO> searchShinryouMaster(String text, String at);

    ShinryouMasterDTO getShinryouMaster(int shinryoucode, LocalDate at);

    VisitFullDTO getVisitFull(int visitId);

    VisitFullPageDTO listVisitFull(int patientId, int page);

    VisitFull2PageDTO listVisitFull2(int patientId, int page);

    ConductDTO getConduct(int conductId);

    ConductFullDTO getConductFull(int conductId);

    List<ConductFullDTO> listConductFullByIds(List<Integer> conductIds);

    void deleteVisitFromReception(int visitId);

    void deleteVisitSafely(int visitId);

    GazouLabelDTO findGazouLabel(int conductId);

    String findGazouLabelString(int conductId);

    void enterGazouLabel(GazouLabelDTO gazoulabelDTO);

    void deleteConduct(int conductId);

    void modifyGazouLabel(int conductId, String label);

    void deleteGazouLabel(int conductId);

    int enterConduct(ConductDTO conductDTO);

    List<ConductDTO> listConducts(int visitId);

    List<ConductFullDTO> listConductFull(int visitId);

    List<ConductShinryouDTO> listConductShinryou(int conductId);

    ConductShinryouFullDTO getConductShinryouFull(int conductShinryouId);

    ConductDrugFullDTO getConductDrugFull(int conductDrugId);

    ConductKizaiFullDTO getConductKizaiFull(int conductKizaiId);

    int enterConductShinryou(ConductShinryouDTO conductShinryouDTO);

    void deleteConductShinryou(int conductShinryouId);

    List<ConductDrugDTO> listConductDrug(int conductId);

    int enterConductDrug(ConductDrugDTO conductDrugDTO);

    void deleteConductDrug(int conductDrugId);

    List<ConductKizaiDTO> listConductKizai(int conductId);

    int enterConductKizai(ConductKizaiDTO conductKizaiDTO);

    void deleteConductKizai(int conductKizaiId);

    Optional<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at);

    Optional<KizaiMasterDTO> findKizaiMasterByKizaicode(int kizaicode, LocalDate at);

    List<KizaiMasterDTO> searchKizaiMasterByName(String text, LocalDate at);

    void modifyConductKind(int conductId, int kind);

    HokenDTO getHokenForVisit(VisitDTO visitDTO);

    List<PaymentVisitPatientDTO> listRecentPayment(int n);

    List<PaymentVisitPatientDTO> listPaymentByPatient(int patientId, int n);

    List<PaymentDTO> listFinalPayment(int n);

    void finishCashier(PaymentDTO paymentDTO);

    Optional<PharmaQueueDTO> findPharmaQueue(int visitId);

    List<PharmaQueueFullDTO> listPharmaQueueFullForPrescription();

    List<PharmaQueueFullDTO> listPharmaQueueFullForToday();

    PharmaQueueFullDTO getPharmaQueueFull(int visitId);

    void deletePharmaQueue(PharmaQueueDTO pharmaQueueDTO);

    PharmaDrugDTO getPharmaDrugByIyakuhincode(int iyakuhincode);

    Optional<PharmaDrugDTO> findPharmaDrugByIyakuhincode(int iyakuhincode);

    List<PharmaDrugDTO> collectPharmaDrugByIyakuhincodes(List<Integer> iyakuhincodes);

    void enterPharmaDrug(PharmaDrugDTO pharmaDrugDTO);

    void updatePharmaDrug(PharmaDrugDTO pharmaDrugDTO);

    void deletePharmaDrug(int iyakuhincode);

    List<PharmaDrugNameDTO> searchPharmaDrugNames(String text);

    List<PharmaDrugNameDTO> listAllPharmaDrugNames();

    List<VisitTextDrugDTO> listVisitTextDrug(List<Integer> visitIds);

    VisitTextDrugPageDTO listVisitTextDrugForPatient(int patientId, int page);

    VisitTextDrugPageDTO listVisitTextDrugByPatientAndIyakuhincode(int patientId, int iyakuhincode, int page);

    Optional<String> findNameForIyakuhincode(int iyakuhincode);

    List<IyakuhinMasterDTO> searchIyakuhinByName(String text, LocalDate at);

    List<IyakuhincodeNameDTO> listIyakuhinForPatient(int patientId);

    List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByIyakuhincodeAndPatientId(int patientId, int iyakuhincode);

    Optional<IyakuhinMasterDTO> findIyakuhinMaster(int iyakuhincode, String at);

    Optional<IyakuhinMasterDTO> findIyakuhinMasterByIyakuhincode(int iyakuhincode, LocalDate at);

    Integer getLastHotlineId();

    List<HotlineDTO> listHotlineInRange(int lowerHotlineId, int upperHotlineId);

    List<HotlineDTO> listTodaysHotlineInRange(int afterId, int beforeId);

    List<HotlineDTO> listRecentHotline(int thresholdHotlineId);

    int enterHotline(HotlineDTO hotlineDTO);

    List<HotlineDTO> listTodaysHotline();

    Optional<HotlineDTO> getTodaysLastHotline();

    PrescExampleDTO findPrescExample(int prescExampleId);

    List<PrescExampleFullDTO> searchPrescExampleFullByName(String text);

    int enterPrescExample(PrescExampleDTO dto);

    void updatePrescExample(PrescExampleDTO dto);

    void deletePrescExample(int prescExampleId);

    List<PrescExampleFullDTO> listAllPrescExample();

    List<DiseaseFullDTO> listCurrentDiseaseFull(int patientId);

    List<DiseaseFullDTO> listDiseaseFull(int patientId);

    long countDiseaseByPatient(int patientId);

    List<DiseaseFullDTO> pageDiseaseFull(int patientId, int page, int itemsPerPage);

    DiseaseFullDTO getDiseaseFull(int diseaseId);

    int enterDisease(DiseaseDTO diseaseDTO, List<DiseaseAdjDTO> adjDTOList);

    void modifyDiseaseEndReason(int diseaseId, LocalDate endDate, char reason);

    void modifyDisease(DiseaseModifyDTO diseaseModifyDTO);

    void deleteDisease(int diseaseId);

    List<ByoumeiMasterDTO> searchByoumeiMaster(String text, LocalDate at);

    Optional<ByoumeiMasterDTO> findByoumeiMasterByName(String name, LocalDate at);

    List<ShuushokugoMasterDTO> searchShuushokugoMaster(String text);

    Optional<ShuushokugoMasterDTO> findShuushokugoMasterByName(String name);

    TextVisitPatientPageDTO searchTextGlobally(String text, int page, int itemsPerPage);

    VisitDrugPageDTO pageVisitIdHavingDrug(int patientId, int page);

    List<VisitChargePatientDTO> listVisitChargePatientAt(LocalDate at);

    List<ShoukiDTO> batchGetShouki(List<Integer> visitIds);

    Optional<ShoukiDTO> findShouki(int visitId);

    void enterShouki(ShoukiDTO shoukiDTO);

    void updateShouki(ShoukiDTO shoukiDTO);

    void deleteShouki(int visitId);

    List<Integer> listVisitingPatientIdHavingHoken(int year, int month);

    List<VisitFull2DTO> listVisitByPatientHavingHoken(int patientId, int year, int month);

    List<DiseaseFullDTO> listDiseaseByPatientAt(int patientId, int year, int month);

    List<PracticeLogDTO> listPracticeLogByDate(LocalDate at);

    List<PracticeLogDTO> listRecentPracticeLog(LocalDate at, int lastId);

    List<PracticeLogDTO> listPracticeLogInRange(LocalDate at, int afterId, int beforeId);

    PracticeLogDTO enterPracticeLog(PracticeLogDTO practiceLogDTO);

    PracticeLogDTO findLastPracticeLog();

    List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds);

    Optional<ShinryouAttrDTO> findShinryouAttr(int shinryouId);

    ShinryouAttrDTO setShinryouTekiyou(int shinryouId, String tekiyou);

    Optional<ShinryouAttrDTO> deleteShinryouTekiyou(int shinryouId);

    void enterShinryouAttr(ShinryouAttrDTO dto);

    List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds);

    Optional<DrugAttrDTO> findDrugAttr(int drugId);

    DrugAttrDTO setDrugTekiyou(int drugId, String tekiyou);

    Optional<DrugAttrDTO> deleteDrugTekiyou(int drugId);

    void enterDrugAttr(DrugAttrDTO dto);
}
