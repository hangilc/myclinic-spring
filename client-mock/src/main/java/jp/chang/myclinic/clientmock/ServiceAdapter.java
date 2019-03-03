package jp.chang.myclinic.clientmock;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServiceAdapter implements Service.ServerAPI {

    @Override
    public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<PatientDTO>> searchPatientCall(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<PatientDTO> getPatientCall(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patientDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updatePatient(PatientDTO patientDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<VisitFull2PageDTO> listVisitFull2Call(int patientId, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitFull2PatientPageDTO> pageVisitFullWithPatientAt(String at, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<VisitFull2PatientPageDTO> pageVisitFullWithPatientAtCall(String at, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> updateTextCall(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterText(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterTextCall(TextDTO textDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextVisitPageDTO> searchTextByPage(int patientId, String text, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<TextVisitPageDTO> searchTextByPageCall(int patientId, String text, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<HokenDTO> listAvailableHokenCall(int patientId, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateHoken(VisitDTO visitDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> updateHokenCall(VisitDTO visitDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<VisitDTO> getVisitCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<HokenDTO> getHokenCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenDTO> convertToHoken(VisitDTO visitDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<HokenDTO> convertToHokenCall(VisitDTO visitDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<IyakuhinMasterDTO>> searchIyakuhinMasterCall(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<PrescExampleFullDTO>> searchPrescExampleCall(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updatePrescExample(PrescExampleDTO prescExample) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deletePrescExample(int prescExampleId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DrugFullDTO>> searchPrevDrugCall(String text, int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<IyakuhinMasterDTO> resolveIyakuhinMaster(int iyakuhincode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<IyakuhinMasterDTO> resolveIyakuhinMasterCall(int iyakuhincode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterDrugCall(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugDTO> getDrug(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DrugDTO> getDrugCall(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DrugFullDTO> getDrugFullCall(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> startVisit(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> startVisitCall(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> startVisit(int patientId, String atDateTime) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteVisit(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteVisitCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<WqueueFullDTO>> listWqueueFullCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<WqueueFullDTO>> listWqueueFullForExamCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisits() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisits(int page, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<VisitPatientDTO>> listRecentVisitsCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisits() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<VisitPatientDTO>> listTodaysVisitsCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> startExam(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> startExamCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> suspendExam(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> suspendExamCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> endExam(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> endExamCall(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextDTO> getText(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<TextDTO> getTextCall(int textId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DrugFullDTO>> listDrugFullCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugFullDTO>> listDrugFullByDrugIds(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DrugFullDTO>> listDrugFullByDrugIdsCall(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<Integer, IyakuhinMasterDTO>> batchResolveIyakuhinMaster(List<Integer> iyakuhincodes, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Map<Integer, IyakuhinMasterDTO>> batchResolveIyakuhinMasterCall(List<Integer> iyakuhincodes, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> batchEnterDrugs(List<DrugDTO> drugs) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<Integer>> batchEnterDrugsCall(List<DrugDTO> drugs) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> batchUpdateDrugDays(List<Integer> drugIds, int days) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> batchUpdateDrugDaysCall(List<Integer> drugIds, int days) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> batchDeleteDrugs(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> batchDeleteDrugsCall(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteDrug(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteDrugCall(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateDrug(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> updateDrugCall(DrugDTO drug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<BatchEnterResultDTO> batchEnterShinryouByName(List<String> names, int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<BatchEnterResultDTO> batchEnterShinryouByNameCall(List<String> names, int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterShinryouCall(ShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateShinryou(ShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> updateShinryouCall(ShinryouDTO shinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteShinryou(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteShinryouCall(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShinryouFullDTO>> listShinryouFullByIdsCall(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShinryouFullDTO>> listShinryouFullCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ConductFullDTO>> listConductFullByIdsCall(List<Integer> conductIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShinryouMasterDTO>> searchShinryouMasterCall(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> resolveShinryoucode(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> resolveShinryoucodeCall(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouMasterDTO> resolveShinryouMasterByNameCall(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMaster(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouMasterDTO> resolveShinryouMasterCall(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<KizaiMasterDTO> resolveKizaiMasterByNameCall(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMaster(int kizaicode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<KizaiMasterDTO> resolveKizaiMasterCall(int kizaicode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouMasterDTO> getShinryouMasterCall(int shinryoucode, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouFullDTO> getShinryouFullCall(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> batchDeleteShinryou(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> batchDeleteShinryouCall(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> batchCopyShinryou(int visitId, List<ShinryouDTO> srcList) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<Integer>> batchCopyShinryouCall(int visitId, List<ShinryouDTO> srcList) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<Integer>> deleteDuplicateShinryouCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductDTO> getConduct(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductDTO> getConductCall(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<GazouLabelDTO> findGazouLabel(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<GazouLabelDTO> findGazouLabelCall(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductFullDTO> getConductFullCall(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO arg) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductFullDTO> enterConductFullCall(ConductEnterRequestDTO arg) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterXp(int visitId, String label, String film) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterXpCall(int visitId, String label, String film) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterInject(int visitId, int conductKindCode, int iyakuhincode, double amount) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterInjectCall(int visitId, int conductKindCode, int iyakuhincode, double amount) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> copyAllConducts(int targetVisitId, int sourceVisitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<Integer>> copyAllConductsCall(int targetVisitId, int sourceVisitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteConduct(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteConductCall(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO conductShinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterConductShinryouCall(ConductShinryouDTO conductShinryou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductShinryouFullDTO> getConductShinryouFullCall(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO conductDrug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterConductDrugCall(ConductDrugDTO conductDrug) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductDrugFullDTO> getConductDrugFullCall(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO conductKizai) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterConductKizaiCall(ConductKizaiDTO conductKizai) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ConductKizaiFullDTO> getConductKizaiFullCall(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<KizaiMasterDTO>> searchKizaiMasterCall(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> modifyConductKind(int conductId, int kind) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> modifyConductKindCall(int conductId, int kind) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> modifyGazouLabel(int conductId, String label) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> modifyGazouLabelCall(int conductId, String label) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteGazouLabel(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteGazouLabelCall(int conductId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteConductShinryou(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteConductShinryouCall(int conductShinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteConductDrug(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteConductDrugCall(int conductDrugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteConductKizai(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteConductKizaiCall(int conductKizaiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> modifyCharge(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> modifyChargeCall(int visitId, int charge) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<MeisaiDTO> getMeisaiCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DiseaseFullDTO>> listCurrentDiseaseFullCall(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DiseaseFullDTO>> listDiseaseFullCall(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> countPageOfDiseaseByPatient(int patientId, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> countPageOfDiseaseByPatientCall(int patientId, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> pageDiseaseFull(int patientId, int page, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DiseaseFullDTO>> pageDiseaseFullCall(int patientId, int page, int itemsPerPage) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DiseaseFullDTO> getDiseaseFullCall(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumei(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ByoumeiMasterDTO>> searchByoumeiCall(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ByoumeiMasterDTO> findByoumeiMasterByName(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ByoumeiMasterDTO> findByoumeiMasterByNameCall(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugo(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShuushokugoMasterDTO>> searchShuushokugoCall(String text) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShuushokugoMasterDTO> findShuushokugoMasterByName(String name) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShuushokugoMasterDTO> findShuushokugoMasterByNameCall(String name) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterDisease(DiseaseNewDTO diseaseNew) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Integer> enterDiseaseCall(DiseaseNewDTO diseaseNew) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DiseaseExampleDTO>> listDiseaseExampleCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> args) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> batchUpdateDiseaseEndReasonCall(List<DiseaseModifyEndReasonDTO> args) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> modifyDiseaseCall(DiseaseModifyDTO diseaseModifyDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteDisease(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteDiseaseCall(int diseaseId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ClinicInfoDTO> getClinicInfoCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ReferItemDTO>> getReferList() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ReferItemDTO>> getReferListCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PracticeConfigDTO> getPracticeConfig() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<PracticeConfigDTO> getPracticeConfigCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitChargePatientDTO>> listVisitChargePatientAt(String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<VisitChargePatientDTO>> listVisitChargePatientAtCall(String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<Integer>> listVisitingPatientIdHavingHoken(int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<Integer>> listVisitingPatientIdHavingHokenCall(int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitFull2DTO>> listVisitByPatientHavingHoken(int patientId, int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<VisitFull2DTO>> listVisitByPatientHavingHokenCall(int patientId, int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseByPatientAt(int patientId, int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DiseaseFullDTO>> listDiseaseByPatientAtCall(int patientId, int year, int month) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouMasterDTO> findShinryouMasterByNameCall(String name, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listAllPracticeLog(String date) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<PracticeLogDTO>> listAllPracticeLogCall(String date) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listAllPracticeLog(String date, int lastId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<PracticeLogDTO>> listAllPracticeLogCall(String date, int lastId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogInRange(String date, int afterId, int beforeId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<PracticeLogDTO>> listPracticeLogInRangeCall(String date, int afterId, int beforeId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShahokokuhoDTO> getShahokokuhoCall(int shahokokuhoId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<KoukikoureiDTO> getKoukikoureiCall(int koukikoureiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<RoujinDTO> getRoujinCall(int roujinId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<KouhiDTO> getKouhiCall(int kouhiId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugForPatient(int patientId, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugByPatientAndIyakuhincode(int patientId, int iyakuhincode, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<IyakuhincodeNameDTO>> listIyakuhinForPatient(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<VisitIdVisitedAtDTO>> listVisitIdVisitedAtByPatientAndIyakuhincode(int patientId, int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PharmaDrugDTO> findPharmaDrug(int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<PharmaDrugDTO> findPharmaDrugCall(int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForPrescription() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForToday() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PharmaQueueFullDTO> getPharmaQueueFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> prescDone(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<PharmaDrugDTO> getPharmaDrug(int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updatePharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deletePharmaDrug(int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMasterByName(String text, String at) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<String> getNameOfIyakuhin(int iyakuhincode) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> enterPharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PharmaDrugNameDTO>> listAllPharmaDrugNames() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<VisitDrugPageDTO> pageVisitDrug(int patientId, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<MeisaiDTO> getVisitMeisai(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ChargeDTO> getCharge(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<HokenListDTO> listHoken(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateKouhi(KouhiDTO kouhiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterKouhi(KouhiDTO kouhiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteRoujin(RoujinDTO roujinDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteKouhi(KouhiDTO kouhiDTO) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PaymentVisitPatientDTO>> listPaymentByPatient(int patientId, int n) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PaymentVisitPatientDTO>> listRecentPayment(int n) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> finishCashier(PaymentDTO payment) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteVisitFromReception(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<WqueueFullDTO> getWqueueFull(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients(int n) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<String>> listHokensho(int patientId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ResponseBody> getHokensho(int patientId, String file) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ResponseBody> getHokenshoCall(int patientId, String file) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShinryouAttrDTO>> batchGetShinryouAttrCall(List<Integer> shinryouIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> findShinryouAttr(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouAttrDTO> findShinryouAttrCall(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> setShinryouTekiyou(int shinryouId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouAttrDTO> setShinryouTekiyouCall(int shinryouId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShinryouAttrDTO> deleteShinryouTekiyou(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShinryouAttrDTO> deleteShinryouTekiyouCall(int shinryouId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> enterShinryouAttr(ShinryouAttrDTO attr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> enterShinryouAttrCall(ShinryouAttrDTO attr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<DrugAttrDTO>> batchGetDrugAttrCall(List<Integer> drugIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugAttrDTO> findDrugAttr(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DrugAttrDTO> findDrugAttrCall(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DrugAttrDTO> setDrugTekiyouCall(int drugId, String tekiyou) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<DrugAttrDTO> deleteDrugTekiyou(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<DrugAttrDTO> deleteDrugTekiyouCall(int drugId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> enterDrugAttr(DrugAttrDTO attr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> enterDrugAttrCall(DrugAttrDTO attr) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<ShoukiDTO>> batchGetShoukiCall(List<Integer> visitIds) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<ShoukiDTO> findShouki(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<ShoukiDTO> findShoukiCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> enterShouki(ShoukiDTO shouki) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> enterShoukiCall(ShoukiDTO shouki) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> updateShouki(ShoukiDTO shouki) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> updateShoukiCall(ShoukiDTO shouki) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Boolean> deleteShouki(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<Boolean> deleteShoukiCall(int visitId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<HotlineDTO>> listTodaysHotline() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<HotlineDTO>> listTodaysHotlineSync() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<HotlineDTO>> listRecentHotline(int thresholdHotlineId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<HotlineDTO>> listRecentHotlineSync(int thresholdHotlineId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Integer> enterHotline(HotlineDTO hotline) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<List<HotlineDTO>> listTodaysHotlineInRange(int afterId, int beforeId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<List<HotlineDTO>> listTodaysHotlineInRangeCall(int afterId, int beforeId) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<StringResultDTO> getPowderDrugConfigFilePath() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<StringResultDTO> getPowderDrugConfigFilePathCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<StringResultDTO> getMasterMapConfigFilePath() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<StringResultDTO> getMasterMapConfigFilePathCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<StringResultDTO> getShinryouByoumeiMapConfigFilePath() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<StringResultDTO> getShinryouByoumeiMapConfigFilePathCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<StringResultDTO> getNameMapConfigFilePath() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Call<StringResultDTO> getNameMapConfigFilePathCall() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(LocalDate at, List<List<String>> args) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(LocalDate at, List<List<String>> args) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveByoumeiNames(LocalDate at, List<List<String>> args) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public CompletableFuture<Map<String, Integer>> batchResolveShuushokugoNames(LocalDate at, List<List<String>> args) {
        throw new RuntimeException("not implemented");
    }
}
