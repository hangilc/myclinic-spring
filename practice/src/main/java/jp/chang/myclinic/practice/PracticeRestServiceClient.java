package jp.chang.myclinic.practice;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PracticeRestServiceClient implements PracticeRestService {

    private Service.ServerAPI api;

    public PracticeRestServiceClient(Service.ServerAPI api){
        this.api = api;
    }

    @GET("search-patient")
    public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
        return api.searchPatient(text);
    }

    @GET("search-patient")
    public Call<List<PatientDTO>> searchPatientCall(String text) {
        return api.searchPatientCall(text);
    }

    @Override
    @GET("get-patient")
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return api.getPatient(patientId);
    }

    @GET("get-patient")
    public Call<PatientDTO> getPatientCall(int patientId) {
        return api.getPatientCall(patientId);
    }

    @POST("enter-patient")
    public CompletableFuture<Integer> enterPatient(PatientDTO patientDTO) {
        return api.enterPatient(patientDTO);
    }

    @POST("update-patient")
    public CompletableFuture<Boolean> updatePatient(PatientDTO patientDTO) {
        return api.updatePatient(patientDTO);
    }

    @GET("list-visit-full2")
    public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
        return api.listVisitFull2(patientId, page);
    }

    @GET("list-visit-full2")
    public Call<VisitFull2PageDTO> listVisitFull2Call(int patientId, int page) {
        return api.listVisitFull2Call(patientId, page);
    }

    @GET("page-visit-full2-with-patient-at")
    public CompletableFuture<VisitFull2PatientPageDTO> pageVisitFullWithPatientAt(String at, int page) {
        return api.pageVisitFullWithPatientAt(at, page);
    }

    @GET("page-visit-full2-with-patient-at")
    public Call<VisitFull2PatientPageDTO> pageVisitFullWithPatientAtCall(String at, int page) {
        return api.pageVisitFullWithPatientAtCall(at, page);
    }

    @Override
    @POST("update-text")
    public CompletableFuture<Boolean> updateText(TextDTO textDTO) {
        return api.updateText(textDTO);
    }

    @POST("update-text")
    public Call<Boolean> updateTextCall(TextDTO textDTO) {
        return api.updateTextCall(textDTO);
    }

    @Override
    @POST("enter-text")
    public CompletableFuture<Integer> enterText(TextDTO textDTO) {
        return api.enterText(textDTO);
    }

    @POST("enter-text")
    public Call<Integer> enterTextCall(TextDTO textDTO) {
        return api.enterTextCall(textDTO);
    }

    @Override
    @POST("delete-text")
    public CompletableFuture<Boolean> deleteText(int textId) {
        return api.deleteText(textId);
    }

    @GET("search-text-by-page")
    public CompletableFuture<TextVisitPageDTO> searchTextByPage(int patientId, String text, int page) {
        return api.searchTextByPage(patientId, text, page);
    }

    @GET("search-text-by-page")
    public Call<TextVisitPageDTO> searchTextByPageCall(int patientId, String text, int page) {
        return api.searchTextByPageCall(patientId, text, page);
    }

    @GET("list-available-hoken")
    public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, String at) {
        return api.listAvailableHoken(patientId, at);
    }

    @GET("list-available-hoken")
    public Call<HokenDTO> listAvailableHokenCall(int patientId, String at) {
        return api.listAvailableHokenCall(patientId, at);
    }

    @POST("update-hoken")
    public CompletableFuture<Boolean> updateHoken(VisitDTO visitDTO) {
        return api.updateHoken(visitDTO);
    }

    @POST("update-hoken")
    public Call<Boolean> updateHokenCall(VisitDTO visitDTO) {
        return api.updateHokenCall(visitDTO);
    }

    @Override
    @GET("get-visit")
    public CompletableFuture<VisitDTO> getVisit(int visitId) {
        return api.getVisit(visitId);
    }

    @GET("get-visit")
    public Call<VisitDTO> getVisitCall(int visitId) {
        return api.getVisitCall(visitId);
    }

    @Override
    @GET("get-hoken")
    public CompletableFuture<HokenDTO> getHoken(int visitId) {
        return api.getHoken(visitId);
    }

    @GET("get-hoken")
    public Call<HokenDTO> getHokenCall(int visitId) {
        return api.getHokenCall(visitId);
    }

    @POST("convert-to-hoken")
    public CompletableFuture<HokenDTO> convertToHoken(VisitDTO visitDTO) {
        return api.convertToHoken(visitDTO);
    }

    @POST("convert-to-hoken")
    public Call<HokenDTO> convertToHokenCall(VisitDTO visitDTO) {
        return api.convertToHokenCall(visitDTO);
    }

    @GET("search-iyakuhin-master-by-name")
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(String text, String at) {
        return api.searchIyakuhinMaster(text, at);
    }

    @GET("search-iyakuhin-master-by-name")
    public Call<List<IyakuhinMasterDTO>> searchIyakuhinMasterCall(String text, String at) {
        return api.searchIyakuhinMasterCall(text, at);
    }

    @GET("search-presc-example-full-by-name")
    public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
        return api.searchPrescExample(text);
    }

    @GET("search-presc-example-full-by-name")
    public Call<List<PrescExampleFullDTO>> searchPrescExampleCall(String text) {
        return api.searchPrescExampleCall(text);
    }

    @POST("enter-presc-example")
    public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
        return api.enterPrescExample(prescExample);
    }

    @POST("update-presc-example")
    public CompletableFuture<Boolean> updatePrescExample(PrescExampleDTO prescExample) {
        return api.updatePrescExample(prescExample);
    }

    @POST("delete-presc-example")
    public CompletableFuture<Boolean> deletePrescExample(int prescExampleId) {
        return api.deletePrescExample(prescExampleId);
    }

    @GET("list-all-presc-example")
    public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
        return api.listAllPrescExample();
    }

    @GET("search-prev-drug")
    public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
        return api.searchPrevDrug(text, patientId);
    }

    @GET("search-prev-drug")
    public Call<List<DrugFullDTO>> searchPrevDrugCall(String text, int patientId) {
        return api.searchPrevDrugCall(text, patientId);
    }

    @GET("resolve-iyakuhin-master")
    public CompletableFuture<IyakuhinMasterDTO> resolveIyakuhinMaster(int iyakuhincode, String at) {
        return api.resolveIyakuhinMaster(iyakuhincode, at);
    }

    @GET("resolve-iyakuhin-master")
    public Call<IyakuhinMasterDTO> resolveIyakuhinMasterCall(int iyakuhincode, String at) {
        return api.resolveIyakuhinMasterCall(iyakuhincode, at);
    }

    @POST("enter-drug")
    public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
        return api.enterDrug(drug);
    }

    @POST("enter-drug")
    public Call<Integer> enterDrugCall(DrugDTO drug) {
        return api.enterDrugCall(drug);
    }

    @GET("get-drug")
    public CompletableFuture<DrugDTO> getDrug(int drugId) {
        return api.getDrug(drugId);
    }

    @GET("get-drug")
    public Call<DrugDTO> getDrugCall(int drugId) {
        return api.getDrugCall(drugId);
    }

    @GET("get-drug-full")
    public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
        return api.getDrugFull(drugId);
    }

    @GET("get-drug-full")
    public Call<DrugFullDTO> getDrugFullCall(int drugId) {
        return api.getDrugFullCall(drugId);
    }

    @POST("start-visit")
    public CompletableFuture<Integer> startVisit(int patientId) {
        return api.startVisit(patientId);
    }

    @POST("start-visit")
    public Call<Integer> startVisitCall(int patientId) {
        return api.startVisitCall(patientId);
    }

    @POST("delete-visit")
    public CompletableFuture<Boolean> deleteVisit(int visitId) {
        return api.deleteVisit(visitId);
    }

    @POST("delete-visit")
    public Call<Boolean> deleteVisitCall(int visitId) {
        return api.deleteVisitCall(visitId);
    }

    @GET("list-wqueue-full")
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
        return api.listWqueueFull();
    }

    @GET("list-wqueue-full")
    public Call<List<WqueueFullDTO>> listWqueueFullCall() {
        return api.listWqueueFullCall();
    }

    @GET("list-wqueue-full-for-exam")
    public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
        return api.listWqueueFullForExam();
    }

    @GET("list-wqueue-full-for-exam")
    public Call<List<WqueueFullDTO>> listWqueueFullForExamCall() {
        return api.listWqueueFullForExamCall();
    }

    @GET("list-visit-with-patient")
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisits() {
        return api.listRecentVisits();
    }

    @GET("list-visit-with-patient")
    public CompletableFuture<List<VisitPatientDTO>> listRecentVisits(int page, int itemsPerPage) {
        return api.listRecentVisits(page, itemsPerPage);
    }

    @GET("list-visit-with-patient")
    public Call<List<VisitPatientDTO>> listRecentVisitsCall() {
        return api.listRecentVisitsCall();
    }

    @GET("list-todays-visits")
    public CompletableFuture<List<VisitPatientDTO>> listTodaysVisits() {
        return api.listTodaysVisits();
    }

    @GET("list-todays-visits")
    public Call<List<VisitPatientDTO>> listTodaysVisitsCall() {
        return api.listTodaysVisitsCall();
    }

    @POST("start-exam")
    public CompletableFuture<Boolean> startExam(int visitId) {
        return api.startExam(visitId);
    }

    @POST("start-exam")
    public Call<Boolean> startExamCall(int visitId) {
        return api.startExamCall(visitId);
    }

    @POST("suspend-exam")
    public CompletableFuture<Boolean> suspendExam(int visitId) {
        return api.suspendExam(visitId);
    }

    @POST("suspend-exam")
    public Call<Boolean> suspendExamCall(int visitId) {
        return api.suspendExamCall(visitId);
    }

    @POST("end-exam")
    public CompletableFuture<Boolean> endExam(int visitId, int charge) {
        return api.endExam(visitId, charge);
    }

    @POST("end-exam")
    public Call<Boolean> endExamCall(int visitId, int charge) {
        return api.endExamCall(visitId, charge);
    }

    @GET("get-text")
    public CompletableFuture<TextDTO> getText(int textId) {
        return api.getText(textId);
    }

    @GET("get-text")
    public Call<TextDTO> getTextCall(int textId) {
        return api.getTextCall(textId);
    }

    @GET("list-drug-full")
    public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
        return api.listDrugFull(visitId);
    }

    @GET("list-drug-full")
    public Call<List<DrugFullDTO>> listDrugFullCall(int visitId) {
        return api.listDrugFullCall(visitId);
    }

    @GET("list-drug-full-by-drug-ids")
    public CompletableFuture<List<DrugFullDTO>> listDrugFullByDrugIds(List<Integer> drugIds) {
        return api.listDrugFullByDrugIds(drugIds);
    }

    @GET("list-drug-full-by-drug-ids")
    public Call<List<DrugFullDTO>> listDrugFullByDrugIdsCall(List<Integer> drugIds) {
        return api.listDrugFullByDrugIdsCall(drugIds);
    }

    @GET("batch-resolve-iyakuhin-master")
    public CompletableFuture<Map<Integer, IyakuhinMasterDTO>> batchResolveIyakuhinMaster(List<Integer> iyakuhincodes, String at) {
        return api.batchResolveIyakuhinMaster(iyakuhincodes, at);
    }

    @GET("batch-resolve-iyakuhin-master")
    public Call<Map<Integer, IyakuhinMasterDTO>> batchResolveIyakuhinMasterCall(List<Integer> iyakuhincodes, String at) {
        return api.batchResolveIyakuhinMasterCall(iyakuhincodes, at);
    }

    @POST("batch-enter-drugs")
    public CompletableFuture<List<Integer>> batchEnterDrugs(List<DrugDTO> drugs) {
        return api.batchEnterDrugs(drugs);
    }

    @POST("batch-enter-drugs")
    public Call<List<Integer>> batchEnterDrugsCall(List<DrugDTO> drugs) {
        return api.batchEnterDrugsCall(drugs);
    }

    @POST("batch-update-drug-days")
    public CompletableFuture<Boolean> batchUpdateDrugDays(List<Integer> drugIds, int days) {
        return api.batchUpdateDrugDays(drugIds, days);
    }

    @POST("batch-update-drug-days")
    public Call<Boolean> batchUpdateDrugDaysCall(List<Integer> drugIds, int days) {
        return api.batchUpdateDrugDaysCall(drugIds, days);
    }

    @POST("batch-delete-drugs")
    public CompletableFuture<Boolean> batchDeleteDrugs(List<Integer> drugIds) {
        return api.batchDeleteDrugs(drugIds);
    }

    @POST("batch-delete-drugs")
    public Call<Boolean> batchDeleteDrugsCall(List<Integer> drugIds) {
        return api.batchDeleteDrugsCall(drugIds);
    }

    @POST("delete-drug")
    public CompletableFuture<Boolean> deleteDrug(int drugId) {
        return api.deleteDrug(drugId);
    }

    @POST("delete-drug")
    public Call<Boolean> deleteDrugCall(int drugId) {
        return api.deleteDrugCall(drugId);
    }

    @POST("update-drug")
    public CompletableFuture<Boolean> updateDrug(DrugDTO drug) {
        return api.updateDrug(drug);
    }

    @POST("update-drug")
    public Call<Boolean> updateDrugCall(DrugDTO drug) {
        return api.updateDrugCall(drug);
    }

    @POST("batch-enter-shinryou-by-name")
    public CompletableFuture<BatchEnterResultDTO> batchEnterShinryouByName(List<String> names, int visitId) {
        return api.batchEnterShinryouByName(names, visitId);
    }

    @POST("batch-enter-shinryou-by-name")
    public Call<BatchEnterResultDTO> batchEnterShinryouByNameCall(List<String> names, int visitId) {
        return api.batchEnterShinryouByNameCall(names, visitId);
    }

    @POST("enter-shinryou")
    public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
        return api.enterShinryou(shinryou);
    }

    @POST("enter-shinryou")
    public Call<Integer> enterShinryouCall(ShinryouDTO shinryou) {
        return api.enterShinryouCall(shinryou);
    }

    @POST("update-shinryou")
    public CompletableFuture<Boolean> updateShinryou(ShinryouDTO shinryou) {
        return api.updateShinryou(shinryou);
    }

    @POST("update-shinryou")
    public Call<Boolean> updateShinryouCall(ShinryouDTO shinryou) {
        return api.updateShinryouCall(shinryou);
    }

    @POST("delete-shinryou")
    public CompletableFuture<Boolean> deleteShinryou(int shinryouId) {
        return api.deleteShinryou(shinryouId);
    }

    @POST("delete-shinryou")
    public Call<Boolean> deleteShinryouCall(int shinryouId) {
        return api.deleteShinryouCall(shinryouId);
    }

    @GET("list-shinryou-full-by-ids")
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
        return api.listShinryouFullByIds(shinryouIds);
    }

    @GET("list-shinryou-full-by-ids")
    public Call<List<ShinryouFullDTO>> listShinryouFullByIdsCall(List<Integer> shinryouIds) {
        return api.listShinryouFullByIdsCall(shinryouIds);
    }

    @GET("list-shinryou-full")
    public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
        return api.listShinryouFull(visitId);
    }

    @GET("list-shinryou-full")
    public Call<List<ShinryouFullDTO>> listShinryouFullCall(int visitId) {
        return api.listShinryouFullCall(visitId);
    }

    @GET("list-conduct-full-by-ids")
    public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
        return api.listConductFullByIds(conductIds);
    }

    @GET("list-conduct-full-by-ids")
    public Call<List<ConductFullDTO>> listConductFullByIdsCall(List<Integer> conductIds) {
        return api.listConductFullByIdsCall(conductIds);
    }

    @GET("search-shinryou-master")
    public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(String text, String at) {
        return api.searchShinryouMaster(text, at);
    }

    @GET("search-shinryou-master")
    public Call<List<ShinryouMasterDTO>> searchShinryouMasterCall(String text, String at) {
        return api.searchShinryouMasterCall(text, at);
    }

    @GET("resolve-shinryoucode")
    public CompletableFuture<Integer> resolveShinryoucode(int shinryoucode, String at) {
        return api.resolveShinryoucode(shinryoucode, at);
    }

    @GET("resolve-shinryoucode")
    public Call<Integer> resolveShinryoucodeCall(int shinryoucode, String at) {
        return api.resolveShinryoucodeCall(shinryoucode, at);
    }

    @GET("resolve-shinryou-master-by-name")
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(String name, String at) {
        return api.resolveShinryouMasterByName(name, at);
    }

    @GET("resolve-shinryou-master-by-name")
    public Call<ShinryouMasterDTO> resolveShinryouMasterByNameCall(String name, String at) {
        return api.resolveShinryouMasterByNameCall(name, at);
    }

    @GET("resolve-shinryou-master")
    public CompletableFuture<ShinryouMasterDTO> resolveShinryouMaster(int shinryoucode, String at) {
        return api.resolveShinryouMaster(shinryoucode, at);
    }

    @GET("resolve-shinryou-master")
    public Call<ShinryouMasterDTO> resolveShinryouMasterCall(int shinryoucode, String at) {
        return api.resolveShinryouMasterCall(shinryoucode, at);
    }

    @GET("resolve-kizai-master-by-name")
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(String name, String at) {
        return api.resolveKizaiMasterByName(name, at);
    }

    @GET("resolve-kizai-master-by-name")
    public Call<KizaiMasterDTO> resolveKizaiMasterByNameCall(String name, String at) {
        return api.resolveKizaiMasterByNameCall(name, at);
    }

    @GET("resolve-kizai-master")
    public CompletableFuture<KizaiMasterDTO> resolveKizaiMaster(int kizaicode, String at) {
        return api.resolveKizaiMaster(kizaicode, at);
    }

    @GET("resolve-kizai-master")
    public Call<KizaiMasterDTO> resolveKizaiMasterCall(int kizaicode, String at) {
        return api.resolveKizaiMasterCall(kizaicode, at);
    }

    @GET("get-shinryou-master")
    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, String at) {
        return api.getShinryouMaster(shinryoucode, at);
    }

    @GET("get-shinryou-master")
    public Call<ShinryouMasterDTO> getShinryouMasterCall(int shinryoucode, String at) {
        return api.getShinryouMasterCall(shinryoucode, at);
    }

    @GET("get-shinryou-full")
    public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
        return api.getShinryouFull(shinryouId);
    }

    @GET("get-shinryou-full")
    public Call<ShinryouFullDTO> getShinryouFullCall(int shinryouId) {
        return api.getShinryouFullCall(shinryouId);
    }

    @POST("batch-delete-shinryou")
    public CompletableFuture<Boolean> batchDeleteShinryou(List<Integer> shinryouIds) {
        return api.batchDeleteShinryou(shinryouIds);
    }

    @POST("batch-delete-shinryou")
    public Call<Boolean> batchDeleteShinryouCall(List<Integer> shinryouIds) {
        return api.batchDeleteShinryouCall(shinryouIds);
    }

    @POST("batch-copy-shinryou")
    public CompletableFuture<List<Integer>> batchCopyShinryou(int visitId, List<ShinryouDTO> srcList) {
        return api.batchCopyShinryou(visitId, srcList);
    }

    @POST("batch-copy-shinryou")
    public Call<List<Integer>> batchCopyShinryouCall(int visitId, List<ShinryouDTO> srcList) {
        return api.batchCopyShinryouCall(visitId, srcList);
    }

    @POST("delete-duplicate-shinryou")
    public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
        return api.deleteDuplicateShinryou(visitId);
    }

    @POST("delete-duplicate-shinryou")
    public Call<List<Integer>> deleteDuplicateShinryouCall(int visitId) {
        return api.deleteDuplicateShinryouCall(visitId);
    }

    @GET("get-conduct")
    public CompletableFuture<ConductDTO> getConduct(int conductId) {
        return api.getConduct(conductId);
    }

    @GET("get-conduct")
    public Call<ConductDTO> getConductCall(int conductId) {
        return api.getConductCall(conductId);
    }

    @GET("get-find-label")
    public CompletableFuture<GazouLabelDTO> findGazouLabel(int conductId) {
        return api.findGazouLabel(conductId);
    }

    @GET("get-find-label")
    public Call<GazouLabelDTO> findGazouLabelCall(int conductId) {
        return api.findGazouLabelCall(conductId);
    }

    @GET("get-conduct-full")
    public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
        return api.getConductFull(conductId);
    }

    @GET("get-conduct-full")
    public Call<ConductFullDTO> getConductFullCall(int conductId) {
        return api.getConductFullCall(conductId);
    }

    @POST("enter-conduct-full")
    public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO arg) {
        return api.enterConductFull(arg);
    }

    @POST("enter-conduct-full")
    public Call<ConductFullDTO> enterConductFullCall(ConductEnterRequestDTO arg) {
        return api.enterConductFullCall(arg);
    }

    @POST("enter-xp")
    public CompletableFuture<Integer> enterXp(int visitId, String label, String film) {
        return api.enterXp(visitId, label, film);
    }

    @POST("enter-xp")
    public Call<Integer> enterXpCall(int visitId, String label, String film) {
        return api.enterXpCall(visitId, label, film);
    }

    @POST("enter-inject")
    public CompletableFuture<Integer> enterInject(int visitId, int conductKindCode, int iyakuhincode, double amount) {
        return api.enterInject(visitId, conductKindCode, iyakuhincode, amount);
    }

    @POST("enter-inject")
    public Call<Integer> enterInjectCall(int visitId, int conductKindCode, int iyakuhincode, double amount) {
        return api.enterInjectCall(visitId, conductKindCode, iyakuhincode, amount);
    }

    @POST("copy-all-conducts")
    public CompletableFuture<List<Integer>> copyAllConducts(int targetVisitId, int sourceVisitId) {
        return api.copyAllConducts(targetVisitId, sourceVisitId);
    }

    @POST("copy-all-conducts")
    public Call<List<Integer>> copyAllConductsCall(int targetVisitId, int sourceVisitId) {
        return api.copyAllConductsCall(targetVisitId, sourceVisitId);
    }

    @POST("delete-conduct")
    public CompletableFuture<Boolean> deleteConduct(int conductId) {
        return api.deleteConduct(conductId);
    }

    @POST("delete-conduct")
    public Call<Boolean> deleteConductCall(int conductId) {
        return api.deleteConductCall(conductId);
    }

    @POST("enter-conduct-shinryou")
    public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO conductShinryou) {
        return api.enterConductShinryou(conductShinryou);
    }

    @POST("enter-conduct-shinryou")
    public Call<Integer> enterConductShinryouCall(ConductShinryouDTO conductShinryou) {
        return api.enterConductShinryouCall(conductShinryou);
    }

    @GET("get-conduct-shinryou-full")
    public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
        return api.getConductShinryouFull(conductShinryouId);
    }

    @GET("get-conduct-shinryou-full")
    public Call<ConductShinryouFullDTO> getConductShinryouFullCall(int conductShinryouId) {
        return api.getConductShinryouFullCall(conductShinryouId);
    }

    @POST("enter-conduct-drug")
    public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO conductDrug) {
        return api.enterConductDrug(conductDrug);
    }

    @POST("enter-conduct-drug")
    public Call<Integer> enterConductDrugCall(ConductDrugDTO conductDrug) {
        return api.enterConductDrugCall(conductDrug);
    }

    @GET("get-conduct-drug-full")
    public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
        return api.getConductDrugFull(conductDrugId);
    }

    @GET("get-conduct-drug-full")
    public Call<ConductDrugFullDTO> getConductDrugFullCall(int conductDrugId) {
        return api.getConductDrugFullCall(conductDrugId);
    }

    @POST("enter-conduct-kizai")
    public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO conductKizai) {
        return api.enterConductKizai(conductKizai);
    }

    @POST("enter-conduct-kizai")
    public Call<Integer> enterConductKizaiCall(ConductKizaiDTO conductKizai) {
        return api.enterConductKizaiCall(conductKizai);
    }

    @GET("get-conduct-kizai-full")
    public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
        return api.getConductKizaiFull(conductKizaiId);
    }

    @GET("get-conduct-kizai-full")
    public Call<ConductKizaiFullDTO> getConductKizaiFullCall(int conductKizaiId) {
        return api.getConductKizaiFullCall(conductKizaiId);
    }

    @GET("search-kizai-master-by-name")
    public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, String at) {
        return api.searchKizaiMaster(text, at);
    }

    @GET("search-kizai-master-by-name")
    public Call<List<KizaiMasterDTO>> searchKizaiMasterCall(String text, String at) {
        return api.searchKizaiMasterCall(text, at);
    }

    @POST("modify-conduct-kind")
    public CompletableFuture<Boolean> modifyConductKind(int conductId, int kind) {
        return api.modifyConductKind(conductId, kind);
    }

    @POST("modify-conduct-kind")
    public Call<Boolean> modifyConductKindCall(int conductId, int kind) {
        return api.modifyConductKindCall(conductId, kind);
    }

    @POST("modify-gazou-label")
    public CompletableFuture<Boolean> modifyGazouLabel(int conductId, String label) {
        return api.modifyGazouLabel(conductId, label);
    }

    @POST("modify-gazou-label")
    public Call<Boolean> modifyGazouLabelCall(int conductId, String label) {
        return api.modifyGazouLabelCall(conductId, label);
    }

    @POST("delete-gazou-label")
    public CompletableFuture<Boolean> deleteGazouLabel(int conductId) {
        return api.deleteGazouLabel(conductId);
    }

    @POST("delete-gazou-label")
    public Call<Boolean> deleteGazouLabelCall(int conductId) {
        return api.deleteGazouLabelCall(conductId);
    }

    @POST("delete-conduct-shinryou")
    public CompletableFuture<Boolean> deleteConductShinryou(int conductShinryouId) {
        return api.deleteConductShinryou(conductShinryouId);
    }

    @POST("delete-conduct-shinryou")
    public Call<Boolean> deleteConductShinryouCall(int conductShinryouId) {
        return api.deleteConductShinryouCall(conductShinryouId);
    }

    @POST("delete-conduct-drug")
    public CompletableFuture<Boolean> deleteConductDrug(int conductDrugId) {
        return api.deleteConductDrug(conductDrugId);
    }

    @POST("delete-conduct-drug")
    public Call<Boolean> deleteConductDrugCall(int conductDrugId) {
        return api.deleteConductDrugCall(conductDrugId);
    }

    @POST("delete-conduct-kizai")
    public CompletableFuture<Boolean> deleteConductKizai(int conductKizaiId) {
        return api.deleteConductKizai(conductKizaiId);
    }

    @POST("delete-conduct-kizai")
    public Call<Boolean> deleteConductKizaiCall(int conductKizaiId) {
        return api.deleteConductKizaiCall(conductKizaiId);
    }

    @POST("modify-charge")
    public CompletableFuture<Boolean> modifyCharge(int visitId, int charge) {
        return api.modifyCharge(visitId, charge);
    }

    @POST("modify-charge")
    public Call<Boolean> modifyChargeCall(int visitId, int charge) {
        return api.modifyChargeCall(visitId, charge);
    }

    @GET("get-visit-meisai")
    public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
        return api.getMeisai(visitId);
    }

    @GET("get-visit-meisai")
    public Call<MeisaiDTO> getMeisaiCall(int visitId) {
        return api.getMeisaiCall(visitId);
    }

    @GET("list-current-disease-full")
    public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
        return api.listCurrentDiseaseFull(patientId);
    }

    @GET("list-current-disease-full")
    public Call<List<DiseaseFullDTO>> listCurrentDiseaseFullCall(int patientId) {
        return api.listCurrentDiseaseFullCall(patientId);
    }

    @GET("list-disease-full")
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
        return api.listDiseaseFull(patientId);
    }

    @GET("list-disease-full")
    public Call<List<DiseaseFullDTO>> listDiseaseFullCall(int patientId) {
        return api.listDiseaseFullCall(patientId);
    }

    @GET("count-page-of-disease-by-patient")
    public CompletableFuture<Integer> countPageOfDiseaseByPatient(int patientId, int itemsPerPage) {
        return api.countPageOfDiseaseByPatient(patientId, itemsPerPage);
    }

    @GET("count-page-of-disease-by-patient")
    public Call<Integer> countPageOfDiseaseByPatientCall(int patientId, int itemsPerPage) {
        return api.countPageOfDiseaseByPatientCall(patientId, itemsPerPage);
    }

    @GET("page-disease-full")
    public CompletableFuture<List<DiseaseFullDTO>> pageDiseaseFull(int patientId, int page, int itemsPerPage) {
        return api.pageDiseaseFull(patientId, page, itemsPerPage);
    }

    @GET("page-disease-full")
    public Call<List<DiseaseFullDTO>> pageDiseaseFullCall(int patientId, int page, int itemsPerPage) {
        return api.pageDiseaseFullCall(patientId, page, itemsPerPage);
    }

    @GET("get-disease-full")
    public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
        return api.getDiseaseFull(diseaseId);
    }

    @GET("get-disease-full")
    public Call<DiseaseFullDTO> getDiseaseFullCall(int diseaseId) {
        return api.getDiseaseFullCall(diseaseId);
    }

    @GET("search-byoumei-master")
    public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumei(String text, String at) {
        return api.searchByoumei(text, at);
    }

    @GET("search-byoumei-master")
    public Call<List<ByoumeiMasterDTO>> searchByoumeiCall(String text, String at) {
        return api.searchByoumeiCall(text, at);
    }

    @GET("find-byoumei-master-by-name")
    public CompletableFuture<ByoumeiMasterDTO> findByoumeiMasterByName(String name, String at) {
        return api.findByoumeiMasterByName(name, at);
    }

    @GET("find-byoumei-master-by-name")
    public Call<ByoumeiMasterDTO> findByoumeiMasterByNameCall(String name, String at) {
        return api.findByoumeiMasterByNameCall(name, at);
    }

    @GET("search-shuushokugo-master")
    public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugo(String text) {
        return api.searchShuushokugo(text);
    }

    @GET("search-shuushokugo-master")
    public Call<List<ShuushokugoMasterDTO>> searchShuushokugoCall(String text) {
        return api.searchShuushokugoCall(text);
    }

    @GET("find-shuushokugo-master-by-name")
    public CompletableFuture<ShuushokugoMasterDTO> findShuushokugoMasterByName(String name) {
        return api.findShuushokugoMasterByName(name);
    }

    @GET("find-shuushokugo-master-by-name")
    public Call<ShuushokugoMasterDTO> findShuushokugoMasterByNameCall(String name) {
        return api.findShuushokugoMasterByNameCall(name);
    }

    @POST("enter-disease")
    public CompletableFuture<Integer> enterDisease(DiseaseNewDTO diseaseNew) {
        return api.enterDisease(diseaseNew);
    }

    @POST("enter-disease")
    public Call<Integer> enterDiseaseCall(DiseaseNewDTO diseaseNew) {
        return api.enterDiseaseCall(diseaseNew);
    }

    @GET("list-disease-example")
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        return api.listDiseaseExample();
    }

    @GET("list-disease-example")
    public Call<List<DiseaseExampleDTO>> listDiseaseExampleCall() {
        return api.listDiseaseExampleCall();
    }

    @POST("batch-update-disease-end-reason")
    public CompletableFuture<Boolean> batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> args) {
        return api.batchUpdateDiseaseEndReason(args);
    }

    @POST("batch-update-disease-end-reason")
    public Call<Boolean> batchUpdateDiseaseEndReasonCall(List<DiseaseModifyEndReasonDTO> args) {
        return api.batchUpdateDiseaseEndReasonCall(args);
    }

    @POST("modify-disease")
    public CompletableFuture<Boolean> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
        return api.modifyDisease(diseaseModifyDTO);
    }

    @POST("modify-disease")
    public Call<Boolean> modifyDiseaseCall(DiseaseModifyDTO diseaseModifyDTO) {
        return api.modifyDiseaseCall(diseaseModifyDTO);
    }

    @POST("delete-disease")
    public CompletableFuture<Boolean> deleteDisease(int diseaseId) {
        return api.deleteDisease(diseaseId);
    }

    @POST("delete-disease")
    public Call<Boolean> deleteDiseaseCall(int diseaseId) {
        return api.deleteDiseaseCall(diseaseId);
    }

    @GET("get-clinic-info")
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        return api.getClinicInfo();
    }

    @GET("get-clinic-info")
    public Call<ClinicInfoDTO> getClinicInfoCall() {
        return api.getClinicInfoCall();
    }

    @GET("get-refer-list")
    public CompletableFuture<List<ReferItemDTO>> getReferList() {
        return api.getReferList();
    }

    @GET("get-refer-list")
    public Call<List<ReferItemDTO>> getReferListCall() {
        return api.getReferListCall();
    }

    @GET("get-practice-config")
    public CompletableFuture<PracticeConfigDTO> getPracticeConfig() {
        return api.getPracticeConfig();
    }

    @GET("get-practice-config")
    public Call<PracticeConfigDTO> getPracticeConfigCall() {
        return api.getPracticeConfigCall();
    }

    @GET("list-visit-charge-patient-at")
    public CompletableFuture<List<VisitChargePatientDTO>> listVisitChargePatientAt(String at) {
        return api.listVisitChargePatientAt(at);
    }

    @GET("list-visit-charge-patient-at")
    public Call<List<VisitChargePatientDTO>> listVisitChargePatientAtCall(String at) {
        return api.listVisitChargePatientAtCall(at);
    }

    @GET("list-visiting-patient-id-having-hoken")
    public CompletableFuture<List<Integer>> listVisitingPatientIdHavingHoken(int year, int month) {
        return api.listVisitingPatientIdHavingHoken(year, month);
    }

    @GET("list-visiting-patient-id-having-hoken")
    public Call<List<Integer>> listVisitingPatientIdHavingHokenCall(int year, int month) {
        return api.listVisitingPatientIdHavingHokenCall(year, month);
    }

    @GET("list-visit-by-patient-having-hoken")
    public CompletableFuture<List<VisitFull2DTO>> listVisitByPatientHavingHoken(int patientId, int year, int month) {
        return api.listVisitByPatientHavingHoken(patientId, year, month);
    }

    @GET("list-visit-by-patient-having-hoken")
    public Call<List<VisitFull2DTO>> listVisitByPatientHavingHokenCall(int patientId, int year, int month) {
        return api.listVisitByPatientHavingHokenCall(patientId, year, month);
    }

    @GET("list-disease-by-patient-at")
    public CompletableFuture<List<DiseaseFullDTO>> listDiseaseByPatientAt(int patientId, int year, int month) {
        return api.listDiseaseByPatientAt(patientId, year, month);
    }

    @GET("list-disease-by-patient-at")
    public Call<List<DiseaseFullDTO>> listDiseaseByPatientAtCall(int patientId, int year, int month) {
        return api.listDiseaseByPatientAtCall(patientId, year, month);
    }

    @GET("find-shinryou-master-by-name")
    public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, String at) {
        return api.findShinryouMasterByName(name, at);
    }

    @GET("find-shinryou-master-by-name")
    public Call<ShinryouMasterDTO> findShinryouMasterByNameCall(String name, String at) {
        return api.findShinryouMasterByNameCall(name, at);
    }

    @GET("list-all-practice-log")
    public CompletableFuture<List<PracticeLogDTO>> listAllPracticeLog(String date) {
        return api.listAllPracticeLog(date);
    }

    @GET("list-all-practice-log")
    public Call<List<PracticeLogDTO>> listAllPracticeLogCall(String date) {
        return api.listAllPracticeLogCall(date);
    }

    @GET("list-practice-log-after")
    public CompletableFuture<List<PracticeLogDTO>> listAllPracticeLog(String date, int lastId) {
        return api.listAllPracticeLog(date, lastId);
    }

    @GET("list-practice-log-after")
    public Call<List<PracticeLogDTO>> listAllPracticeLogCall(String date, int lastId) {
        return api.listAllPracticeLogCall(date, lastId);
    }

    @GET("list-practice-log-in-range")
    public CompletableFuture<List<PracticeLogDTO>> listPracticeLogInRange(String date, int afterId, int beforeId) {
        return api.listPracticeLogInRange(date, afterId, beforeId);
    }

    @GET("list-practice-log-in-range")
    public Call<List<PracticeLogDTO>> listPracticeLogInRangeCall(String date, int afterId, int beforeId) {
        return api.listPracticeLogInRangeCall(date, afterId, beforeId);
    }

    @GET("get-shahokokuho")
    public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
        return api.getShahokokuho(shahokokuhoId);
    }

    @GET("get-shahokokuho")
    public Call<ShahokokuhoDTO> getShahokokuhoCall(int shahokokuhoId) {
        return api.getShahokokuhoCall(shahokokuhoId);
    }

    @GET("get-koukikourei")
    public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
        return api.getKoukikourei(koukikoureiId);
    }

    @GET("get-koukikourei")
    public Call<KoukikoureiDTO> getKoukikoureiCall(int koukikoureiId) {
        return api.getKoukikoureiCall(koukikoureiId);
    }

    @GET("get-roujin")
    public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
        return api.getRoujin(roujinId);
    }

    @GET("get-roujin")
    public Call<RoujinDTO> getRoujinCall(int roujinId) {
        return api.getRoujinCall(roujinId);
    }

    @GET("get-kouhi")
    public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
        return api.getKouhi(kouhiId);
    }

    @GET("get-kouhi")
    public Call<KouhiDTO> getKouhiCall(int kouhiId) {
        return api.getKouhiCall(kouhiId);
    }

    @GET("list-visit-text-drug-for-patient")
    public CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugForPatient(int patientId, int page) {
        return api.listVisitTextDrugForPatient(patientId, page);
    }

    @GET("list-visit-text-drug-by-patient-and-iyakuhincode")
    public CompletableFuture<VisitTextDrugPageDTO> listVisitTextDrugByPatientAndIyakuhincode(int patientId, int iyakuhincode, int page) {
        return api.listVisitTextDrugByPatientAndIyakuhincode(patientId, iyakuhincode, page);
    }

    @GET("list-iyakuhin-for-patient")
    public CompletableFuture<List<IyakuhincodeNameDTO>> listIyakuhinForPatient(int patientId) {
        return api.listIyakuhinForPatient(patientId);
    }

    @GET("list-visit-id-visited-at-by-patient-and-iyakuhincode")
    public CompletableFuture<List<VisitIdVisitedAtDTO>> listVisitIdVisitedAtByPatientAndIyakuhincode(int patientId, int iyakuhincode) {
        return api.listVisitIdVisitedAtByPatientAndIyakuhincode(patientId, iyakuhincode);
    }

    @GET("find-pharma-drug")
    public CompletableFuture<PharmaDrugDTO> findPharmaDrug(int iyakuhincode) {
        return api.findPharmaDrug(iyakuhincode);
    }

    @GET("find-pharma-drug")
    public Call<PharmaDrugDTO> findPharmaDrugCall(int iyakuhincode) {
        return api.findPharmaDrugCall(iyakuhincode);
    }

    @GET("list-pharma-queue-full-for-prescription")
    public CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForPrescription() {
        return api.listPharmaQueueForPrescription();
    }

    @GET("list-pharma-queue-full-for-today")
    public CompletableFuture<List<PharmaQueueFullDTO>> listPharmaQueueForToday() {
        return api.listPharmaQueueForToday();
    }

    @GET("get-pharma-queue-full")
    public CompletableFuture<PharmaQueueFullDTO> getPharmaQueueFull(int visitId) {
        return api.getPharmaQueueFull(visitId);
    }

    @POST("presc-done")
    public CompletableFuture<Boolean> prescDone(int visitId) {
        return api.prescDone(visitId);
    }

    @GET("get-pharma-drug")
    public CompletableFuture<PharmaDrugDTO> getPharmaDrug(int iyakuhincode) {
        return api.getPharmaDrug(iyakuhincode);
    }

    @POST("update-pharma-drug")
    public CompletableFuture<Boolean> updatePharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        return api.updatePharmaDrug(pharmaDrugDTO);
    }

    @POST("delete-pharma-drug")
    public CompletableFuture<Boolean> deletePharmaDrug(int iyakuhincode) {
        return api.deletePharmaDrug(iyakuhincode);
    }

    @GET("search-iyakuhin-master-by-name")
    public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMasterByName(String text, String at) {
        return api.searchIyakuhinMasterByName(text, at);
    }

    @GET("get-name-of-iyakuhin")
    public CompletableFuture<String> getNameOfIyakuhin(int iyakuhincode) {
        return api.getNameOfIyakuhin(iyakuhincode);
    }

    @POST("enter-pharma-drug")
    public CompletableFuture<Boolean> enterPharmaDrug(PharmaDrugDTO pharmaDrugDTO) {
        return api.enterPharmaDrug(pharmaDrugDTO);
    }

    @GET("list-all-pharma-drug-names")
    public CompletableFuture<List<PharmaDrugNameDTO>> listAllPharmaDrugNames() {
        return api.listAllPharmaDrugNames();
    }

    @GET("page-visit-drug")
    public CompletableFuture<VisitDrugPageDTO> pageVisitDrug(int patientId, int page) {
        return api.pageVisitDrug(patientId, page);
    }

    @GET("get-visit-meisai")
    public CompletableFuture<MeisaiDTO> getVisitMeisai(int visitId) {
        return api.getVisitMeisai(visitId);
    }

    @GET("list-payment")
    public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
        return api.listPayment(visitId);
    }

    @GET("get-charge")
    public CompletableFuture<ChargeDTO> getCharge(int visitId) {
        return api.getCharge(visitId);
    }

    @GET("list-hoken")
    public CompletableFuture<HokenListDTO> listHoken(int patientId) {
        return api.listHoken(patientId);
    }

    @POST("update-shahokokuho")
    public CompletableFuture<Boolean> updateShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        return api.updateShahokokuho(shahokokuhoDTO);
    }

    @POST("update-koukikourei")
    public CompletableFuture<Boolean> updateKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        return api.updateKoukikourei(koukikoureiDTO);
    }

    @POST("update-kouhi")
    public CompletableFuture<Boolean> updateKouhi(KouhiDTO kouhiDTO) {
        return api.updateKouhi(kouhiDTO);
    }

    @POST("enter-shahokokuho")
    public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        return api.enterShahokokuho(shahokokuhoDTO);
    }

    @POST("enter-koukikourei")
    public CompletableFuture<Integer> enterKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        return api.enterKoukikourei(koukikoureiDTO);
    }

    @POST("enter-kouhi")
    public CompletableFuture<Integer> enterKouhi(KouhiDTO kouhiDTO) {
        return api.enterKouhi(kouhiDTO);
    }

    @POST("delete-shahokokuho")
    public CompletableFuture<Boolean> deleteShahokokuho(ShahokokuhoDTO shahokokuhoDTO) {
        return api.deleteShahokokuho(shahokokuhoDTO);
    }

    @POST("delete-koukikourei")
    public CompletableFuture<Boolean> deleteKoukikourei(KoukikoureiDTO koukikoureiDTO) {
        return api.deleteKoukikourei(koukikoureiDTO);
    }

    @POST("delete-roujin")
    public CompletableFuture<Boolean> deleteRoujin(RoujinDTO roujinDTO) {
        return api.deleteRoujin(roujinDTO);
    }

    @POST("delete-kouhi")
    public CompletableFuture<Boolean> deleteKouhi(KouhiDTO kouhiDTO) {
        return api.deleteKouhi(kouhiDTO);
    }

    @GET("list-payment-by-patient")
    public CompletableFuture<List<PaymentVisitPatientDTO>> listPaymentByPatient(int patientId, int n) {
        return api.listPaymentByPatient(patientId, n);
    }

    @GET("list-recent-payment")
    public CompletableFuture<List<PaymentVisitPatientDTO>> listRecentPayment(int n) {
        return api.listRecentPayment(n);
    }

    @POST("finish-cashier")
    public CompletableFuture<Boolean> finishCashier(PaymentDTO payment) {
        return api.finishCashier(payment);
    }

    @POST("delete-visit-from-reception")
    public CompletableFuture<Boolean> deleteVisitFromReception(int visitId) {
        return api.deleteVisitFromReception(visitId);
    }

    @GET("get-wqueue-full")
    public CompletableFuture<WqueueFullDTO> getWqueueFull(int visitId) {
        return api.getWqueueFull(visitId);
    }

    @GET("list-recently-registered-patients")
    public CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients() {
        return api.listRecentlyRegisteredPatients();
    }

    @GET("list-recently-registered-patients")
    public CompletableFuture<List<PatientDTO>> listRecentlyRegisteredPatients(int n) {
        return api.listRecentlyRegisteredPatients(n);
    }

    @GET("list-hokensho")
    public CompletableFuture<List<String>> listHokensho(int patientId) {
        return api.listHokensho(patientId);
    }

    @GET("get-hokensho")
    @Streaming
    public CompletableFuture<ResponseBody> getHokensho(int patientId, String file) {
        return api.getHokensho(patientId, file);
    }

    @GET("get-hokensho")
    @Streaming
    public Call<ResponseBody> getHokenshoCall(int patientId, String file) {
        return api.getHokenshoCall(patientId, file);
    }

    @Override
    @GET("batch-get-shinryou-attr")
    public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return api.batchGetShinryouAttr(shinryouIds);
    }

    @GET("batch-get-shinryou-attr")
    public Call<List<ShinryouAttrDTO>> batchGetShinryouAttrCall(List<Integer> shinryouIds) {
        return api.batchGetShinryouAttrCall(shinryouIds);
    }

    @GET("find-shinryou-attr")
    public CompletableFuture<ShinryouAttrDTO> findShinryouAttr(int shinryouId) {
        return api.findShinryouAttr(shinryouId);
    }

    @GET("find-shinryou-attr")
    public Call<ShinryouAttrDTO> findShinryouAttrCall(int shinryouId) {
        return api.findShinryouAttrCall(shinryouId);
    }

    @POST("set-shinryou-tekiyou")
    public CompletableFuture<ShinryouAttrDTO> setShinryouTekiyou(int shinryouId, String tekiyou) {
        return api.setShinryouTekiyou(shinryouId, tekiyou);
    }

    @POST("set-shinryou-tekiyou")
    public Call<ShinryouAttrDTO> setShinryouTekiyouCall(int shinryouId, String tekiyou) {
        return api.setShinryouTekiyouCall(shinryouId, tekiyou);
    }

    @POST("delete-shinryou-tekiyou")
    public CompletableFuture<ShinryouAttrDTO> deleteShinryouTekiyou(int shinryouId) {
        return api.deleteShinryouTekiyou(shinryouId);
    }

    @POST("delete-shinryou-tekiyou")
    public Call<ShinryouAttrDTO> deleteShinryouTekiyouCall(int shinryouId) {
        return api.deleteShinryouTekiyouCall(shinryouId);
    }

    @POST("enter-shinryou-attr")
    public CompletableFuture<Boolean> enterShinryouAttr(ShinryouAttrDTO attr) {
        return api.enterShinryouAttr(attr);
    }

    @POST("enter-shinryou-attr")
    public Call<Boolean> enterShinryouAttrCall(ShinryouAttrDTO attr) {
        return api.enterShinryouAttrCall(attr);
    }

    @Override
    @GET("batch-get-drug-attr")
    public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
        return api.batchGetDrugAttr(drugIds);
    }

    @GET("batch-get-drug-attr")
    public Call<List<DrugAttrDTO>> batchGetDrugAttrCall(List<Integer> drugIds) {
        return api.batchGetDrugAttrCall(drugIds);
    }

    @GET("find-drug-attr")
    public CompletableFuture<DrugAttrDTO> findDrugAttr(int drugId) {
        return api.findDrugAttr(drugId);
    }

    @GET("find-drug-attr")
    public Call<DrugAttrDTO> findDrugAttrCall(int drugId) {
        return api.findDrugAttrCall(drugId);
    }

    @POST("set-drug-tekiyou")
    public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
        return api.setDrugTekiyou(drugId, tekiyou);
    }

    @POST("set-drug-tekiyou")
    public Call<DrugAttrDTO> setDrugTekiyouCall(int drugId, String tekiyou) {
        return api.setDrugTekiyouCall(drugId, tekiyou);
    }

    @POST("delete-drug-tekiyou")
    public CompletableFuture<DrugAttrDTO> deleteDrugTekiyou(int drugId) {
        return api.deleteDrugTekiyou(drugId);
    }

    @POST("delete-drug-tekiyou")
    public Call<DrugAttrDTO> deleteDrugTekiyouCall(int drugId) {
        return api.deleteDrugTekiyouCall(drugId);
    }

    @POST("enter-drug-attr")
    public CompletableFuture<Boolean> enterDrugAttr(DrugAttrDTO attr) {
        return api.enterDrugAttr(attr);
    }

    @POST("enter-drug-attr")
    public Call<Boolean> enterDrugAttrCall(DrugAttrDTO attr) {
        return api.enterDrugAttrCall(attr);
    }

    @Override
    @GET("batchy-get-shouki")
    public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
        return api.batchGetShouki(visitIds);
    }

    @GET("batchy-get-shouki")
    public Call<List<ShoukiDTO>> batchGetShoukiCall(List<Integer> visitIds) {
        return api.batchGetShoukiCall(visitIds);
    }

    @GET("find-shouki")
    public CompletableFuture<ShoukiDTO> findShouki(int visitId) {
        return api.findShouki(visitId);
    }

    @GET("find-shouki")
    public Call<ShoukiDTO> findShoukiCall(int visitId) {
        return api.findShoukiCall(visitId);
    }

    @POST("enter-shouki")
    public CompletableFuture<Boolean> enterShouki(ShoukiDTO shouki) {
        return api.enterShouki(shouki);
    }

    @POST("enter-shouki")
    public Call<Boolean> enterShoukiCall(ShoukiDTO shouki) {
        return api.enterShoukiCall(shouki);
    }

    @POST("update-shouki")
    public CompletableFuture<Boolean> updateShouki(ShoukiDTO shouki) {
        return api.updateShouki(shouki);
    }

    @POST("update-shouki")
    public Call<Boolean> updateShoukiCall(ShoukiDTO shouki) {
        return api.updateShoukiCall(shouki);
    }

    @POST("delete-shouki")
    public CompletableFuture<Boolean> deleteShouki(int visitId) {
        return api.deleteShouki(visitId);
    }

    @POST("delete-shouki")
    public Call<Boolean> deleteShoukiCall(int visitId) {
        return api.deleteShoukiCall(visitId);
    }

    @GET("search-text-globally")
    public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
        return api.searchTextGlobally(text, page);
    }

    @GET("list-todays-hotline")
    public CompletableFuture<List<HotlineDTO>> listTodaysHotline() {
        return api.listTodaysHotline();
    }

    @GET("list-todays-hotline")
    public Call<List<HotlineDTO>> listTodaysHotlineSync() {
        return api.listTodaysHotlineSync();
    }

    @GET("list-recent-hotline")
    public CompletableFuture<List<HotlineDTO>> listRecentHotline(int thresholdHotlineId) {
        return api.listRecentHotline(thresholdHotlineId);
    }

    @GET("list-recent-hotline")
    public Call<List<HotlineDTO>> listRecentHotlineSync(int thresholdHotlineId) {
        return api.listRecentHotlineSync(thresholdHotlineId);
    }

    @POST("enter-hotline")
    public CompletableFuture<Integer> enterHotline(HotlineDTO hotline) {
        return api.enterHotline(hotline);
    }

    @GET("list-todays-hotline-in-range")
    public CompletableFuture<List<HotlineDTO>> listTodaysHotlineInRange(int afterId, int beforeId) {
        return api.listTodaysHotlineInRange(afterId, beforeId);
    }

    @GET("list-todays-hotline-in-range")
    public Call<List<HotlineDTO>> listTodaysHotlineInRangeCall(int afterId, int beforeId) {
        return api.listTodaysHotlineInRangeCall(afterId, beforeId);
    }

    @GET("get-powder-drug-config-file-path")
    public CompletableFuture<StringResultDTO> getPowderDrugConfigFilePath() {
        return api.getPowderDrugConfigFilePath();
    }

    @GET("get-powder-drug-config-file-path")
    public Call<StringResultDTO> getPowderDrugConfigFilePathCall() {
        return api.getPowderDrugConfigFilePathCall();
    }

    @GET("get-master-map-config-file-path")
    public CompletableFuture<StringResultDTO> getMasterMapConfigFilePath() {
        return api.getMasterMapConfigFilePath();
    }

    @GET("get-master-map-config-file-path")
    public Call<StringResultDTO> getMasterMapConfigFilePathCall() {
        return api.getMasterMapConfigFilePathCall();
    }

    @GET("get-shinryou-byoumei-map-config-file-path")
    public CompletableFuture<StringResultDTO> getShinryouByoumeiMapConfigFilePath() {
        return api.getShinryouByoumeiMapConfigFilePath();
    }

    @GET("get-shinryou-byoumei-map-config-file-path")
    public Call<StringResultDTO> getShinryouByoumeiMapConfigFilePathCall() {
        return api.getShinryouByoumeiMapConfigFilePathCall();
    }

    @GET("get-name-map-config-file-path")
    public CompletableFuture<StringResultDTO> getNameMapConfigFilePath() {
        return api.getNameMapConfigFilePath();
    }

    @GET("get-name-map-config-file-path")
    public Call<StringResultDTO> getNameMapConfigFilePathCall() {
        return api.getNameMapConfigFilePathCall();
    }

    @POST("batch-resolve-shinryou-names")
    public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(LocalDate at, List<List<String>> args) {
        return api.batchResolveShinryouNames(at, args);
    }

    @POST("batch-resolve-kizai-names")
    public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(LocalDate at, List<List<String>> args) {
        return api.batchResolveKizaiNames(at, args);
    }

    @POST("batch-resolve-byoumei-names")
    public CompletableFuture<Map<String, Integer>> batchResolveByoumeiNames(LocalDate at, List<List<String>> args) {
        return api.batchResolveByoumeiNames(at, args);
    }

    @POST("batch-resolve-shuushokugo-names")
    public CompletableFuture<Map<String, Integer>> batchResolveShuushokugoNames(LocalDate at, List<List<String>> args) {
        return api.batchResolveShuushokugoNames(at, args);
    }
}
