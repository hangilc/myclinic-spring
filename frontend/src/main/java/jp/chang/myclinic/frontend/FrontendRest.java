package jp.chang.myclinic.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class FrontendRest {

  static ExecutorService executorService = Executors.newFixedThreadPool(10);

  private static ObjectMapper mapper = new ObjectMapper();

  private static ClientConfig clientConfig = new ClientConfig();

  static {
    clientConfig.executorService(executorService);
    clientConfig.register(
        new JacksonJaxbJsonProvider(mapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
  }

  interface ParamSetter {

    void set(String name, Object value);
  }

  private String serverUrl;

  @SuppressWarnings("WeakerAccess")
  public FrontendRest(String serverUrl) {
    this.serverUrl = serverUrl;
  }

  private CompletionStageRxInvoker call(String path, Consumer<ParamSetter> paramSetter) {
    class Local {

      private WebTarget target;
    }
    Local local = new Local();
    local.target = ClientBuilder.newClient(clientConfig).target(serverUrl).path(path);
    paramSetter.accept((name, value) -> local.target = local.target.queryParam(name, value));
    return local.target.request(MediaType.APPLICATION_JSON).rx();
  }

  private <T> CompletableFuture<T> get(
      String path, Consumer<ParamSetter> paramSetter, GenericType<T> returnType) {
    return call(path, paramSetter).get(returnType).toCompletableFuture();
  }

  private <T> CompletableFuture<T> post(
      String path, Consumer<ParamSetter> paramSetter, Object body, GenericType<T> returnType) {
    return call(path, paramSetter)
        .post(Entity.entity(body, MediaType.APPLICATION_JSON), returnType)
        .toCompletableFuture();
  }

  public CompletableFuture<PatientDTO> getPatient(int patientId) {
    return get(
        "get-patient",
        setter -> setter.set("patient-id", patientId),
        new GenericType<PatientDTO>() {});
  }

  public CompletableFuture<Integer> enterPatient(PatientDTO patient) {
    return post(
        "enter-patient",
        setter -> {},
        Entity.entity(patient, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<Void> updatePatient(PatientDTO patient) {
    return post(
        "update-patient",
        setter -> {},
        Entity.entity(patient, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<PatientDTO>> searchPatientByKeyword2(
      String lastNameKeyword, String firstNameKeyword) {
    return get(
        "search-patient-by-keyword2",
        setter -> {
          setter.set("last-name-keyword", lastNameKeyword);
          setter.set("first-name-keyword", firstNameKeyword);
        },
        new GenericType<List<PatientDTO>>() {});
  }

  public CompletableFuture<List<PatientDTO>> searchPatientByKeyword(String keyword) {
    return get(
        "search-patient-by-keyword",
        setter -> setter.set("keyword", keyword),
        new GenericType<List<PatientDTO>>() {});
  }

  public CompletableFuture<List<PatientDTO>> searchPatient(String text) {
    return get(
        "search-patient",
        setter -> setter.set("text", text),
        new GenericType<List<PatientDTO>>() {});
  }

  public CompletableFuture<List<ShahokokuhoDTO>> findAvailableShahokokuho(
      int patientId, LocalDate at) {
    return get(
        "find-available-shahokokuho",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("at", at);
        },
        new GenericType<List<ShahokokuhoDTO>>() {});
  }

  public CompletableFuture<List<KoukikoureiDTO>> findAvailableKoukikourei(
      int patientId, LocalDate at) {
    return get(
        "find-available-koukikourei",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("at", at);
        },
        new GenericType<List<KoukikoureiDTO>>() {});
  }

  public CompletableFuture<List<RoujinDTO>> findAvailableRoujin(int patientId, LocalDate at) {
    return get(
        "find-available-roujin",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("at", at);
        },
        new GenericType<List<RoujinDTO>>() {});
  }

  public CompletableFuture<List<KouhiDTO>> findAvailableKouhi(int patientId, LocalDate at) {
    return get(
        "find-available-kouhi",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("at", at);
        },
        new GenericType<List<KouhiDTO>>() {});
  }

  public CompletableFuture<VisitDTO> startVisit(int patientId, LocalDateTime at) {
    return get(
        "start-visit",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("at", at);
        },
        new GenericType<VisitDTO>() {});
  }

  public CompletableFuture<Void> startExam(int visitId) {
    return post(
        "start-exam",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> suspendExam(int visitId) {
    return post(
        "suspend-exam",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> endExam(int visitId, int charge) {
    return post(
        "end-exam",
        setter -> {
          setter.set("visit-id", visitId);
          setter.set("charge", charge);
        },
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> enterCharge(ChargeDTO charge) {
    return post(
        "enter-charge",
        setter -> {},
        Entity.entity(charge, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> setChargeOfVisit(int visitId, int charge) {
    return post(
        "set-charge-of-visit",
        setter -> {
          setter.set("visit-id", visitId);
          setter.set("charge", charge);
        },
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<ChargeDTO> getCharge(int visitId) {
    return get(
        "get-charge", setter -> setter.set("visit-id", visitId), new GenericType<ChargeDTO>() {});
  }

  public CompletableFuture<List<PaymentDTO>> listPayment(int visitId) {
    return get(
        "list-payment",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<PaymentDTO>>() {});
  }

  public CompletableFuture<WqueueDTO> getWqueue(int visitId) {
    return get(
        "get-wqueue", setter -> setter.set("visit-id", visitId), new GenericType<WqueueDTO>() {});
  }

  public CompletableFuture<Void> deleteWqueue(int visitId) {
    return post(
        "delete-wqueue",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<WqueueDTO>> listWqueue() {
    return get("list-wqueue", setter -> {}, new GenericType<List<WqueueDTO>>() {});
  }

  public CompletableFuture<List<WqueueFullDTO>> listWqueueFull() {
    return get("list-wqueue-full", setter -> {}, new GenericType<List<WqueueFullDTO>>() {});
  }

  public CompletableFuture<List<WqueueFullDTO>> listWqueueFullForExam() {
    return get(
        "list-wqueue-full-for-exam", setter -> {}, new GenericType<List<WqueueFullDTO>>() {});
  }

  public CompletableFuture<HokenDTO> getHoken(int visitId) {
    return get(
        "get-hoken", setter -> setter.set("visit-id", visitId), new GenericType<HokenDTO>() {});
  }

  public CompletableFuture<HokenDTO> listAvailableHoken(int patientId, LocalDate visitedAt) {
    return get(
        "list-available-hoken",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("visited-at", visitedAt);
        },
        new GenericType<HokenDTO>() {});
  }

  public CompletableFuture<Void> updateHoken(VisitDTO visit) {
    return post(
        "update-hoken",
        setter -> {},
        Entity.entity(visit, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<DrugDTO> getDrug(int drugId) {
    return get("get-drug", setter -> setter.set("drug-id", drugId), new GenericType<DrugDTO>() {});
  }

  public CompletableFuture<DrugWithAttrDTO> getDrugWithAttr(int drugId) {
    return get(
        "get-drug-with-attr",
        setter -> setter.set("drug-id", drugId),
        new GenericType<DrugWithAttrDTO>() {});
  }

  public CompletableFuture<Integer> enterDrug(DrugDTO drug) {
    return post(
        "enter-drug",
        setter -> {},
        Entity.entity(drug, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<Void> updateDrug(DrugDTO drug) {
    return post(
        "update-drug",
        setter -> {},
        Entity.entity(drug, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> batchUpdateDrugDays(List<Integer> drugIds, int days) {
    return post(
        "batch-update-drug-days",
        setter -> setter.set("days", days),
        Entity.entity(drugIds, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteDrug(int drugId) {
    return post(
        "delete-drug",
        setter -> setter.set("drug-id", drugId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteDrugCascading(int drugId) {
    return post(
        "delete-drug-cascading",
        setter -> setter.set("drug-id", drugId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> batchDeleteDrugs(List<Integer> drugIds) {
    return post(
        "batch-delete-drugs",
        setter -> {},
        Entity.entity(drugIds, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<DrugFullDTO> getDrugFull(int drugId) {
    return get(
        "get-drug-full",
        setter -> setter.set("drug-id", drugId),
        new GenericType<DrugFullDTO>() {});
  }

  public CompletableFuture<DrugFullWithAttrDTO> getDrugFullWithAttr(int drugId) {
    return get(
        "get-drug-full-with-attr",
        setter -> setter.set("drug-id", drugId),
        new GenericType<DrugFullWithAttrDTO>() {});
  }

  public CompletableFuture<List<DrugWithAttrDTO>> listDrugWithAttr(int visitId) {
    return get(
        "list-drug-with-attr",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<DrugWithAttrDTO>>() {});
  }

  public CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId) {
    return get(
        "list-drug-full",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<DrugFullDTO>>() {});
  }

  public CompletableFuture<List<Integer>> listRepresentativeGaiyouDrugId(
      String text, int patientId) {
    return get(
        "list-representative-gaiyou-drug-id",
        setter -> {
          setter.set("text", text);
          setter.set("patient-id", patientId);
        },
        new GenericType<List<Integer>>() {});
  }

  public CompletableFuture<List<DrugFullDTO>> listPrevDrugByPatient(int patientId) {
    return get(
        "list-prev-drug-by-patient",
        setter -> setter.set("patient-id", patientId),
        new GenericType<List<DrugFullDTO>>() {});
  }

  public CompletableFuture<List<DrugFullDTO>> searchPrevDrug(String text, int patientId) {
    return get(
        "search-prev-drug",
        setter -> {
          setter.set("text", text);
          setter.set("patient-id", patientId);
        },
        new GenericType<List<DrugFullDTO>>() {});
  }

  public CompletableFuture<Integer> countUnprescribedDrug(int visitId) {
    return get(
        "count-unprescribed-drug",
        setter -> setter.set("visit-id", visitId),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<Void> markDrugsAsPrescribed(int visitId) {
    return post(
        "mark-drugs-as-prescribed",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<DrugAttrDTO> getDrugAttr(int drugId) {
    return get(
        "get-drug-attr",
        setter -> setter.set("drug-id", drugId),
        new GenericType<DrugAttrDTO>() {});
  }

  public CompletableFuture<Void> enterDrugAttr(DrugAttrDTO drugAttr) {
    return post(
        "enter-drug-attr",
        setter -> {},
        Entity.entity(drugAttr, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> updateDrugAttr(DrugAttrDTO drugAttr) {
    return post(
        "update-drug-attr",
        setter -> {},
        Entity.entity(drugAttr, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteDrugAttr(int drugId) {
    return post(
        "delete-drug-attr",
        setter -> setter.set("drug-id", drugId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<DrugAttrDTO>> batchGetDrugAttr(List<Integer> drugIds) {
    return post(
        "batch-get-drug-attr",
        setter -> {},
        Entity.entity(drugIds, MediaType.APPLICATION_JSON),
        new GenericType<List<DrugAttrDTO>>() {});
  }

  public CompletableFuture<DrugAttrDTO> setDrugTekiyou(int drugId, String tekiyou) {
    return get(
        "set-drug-tekiyou",
        setter -> {
          setter.set("drug-id", drugId);
          setter.set("tekiyou", tekiyou);
        },
        new GenericType<DrugAttrDTO>() {});
  }

  public CompletableFuture<Void> deleteDrugTekiyou(int drugId) {
    return post(
        "delete-drug-tekiyou",
        setter -> setter.set("drug-id", drugId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<VisitDTO> getVisit(int visitId) {
    return get(
        "get-visit", setter -> setter.set("visit-id", visitId), new GenericType<VisitDTO>() {});
  }

  public CompletableFuture<Void> deleteVisitSafely(int visitId) {
    return post(
        "delete-visit-safely",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<VisitPatientDTO>> listRecentVisitWithPatient(
      int page, int itemsPerPage) {
    return get(
        "list-recent-visit-with-patient",
        setter -> {
          setter.set("page", page);
          setter.set("items-per-page", itemsPerPage);
        },
        new GenericType<List<VisitPatientDTO>>() {});
  }

  public CompletableFuture<List<VisitPatientDTO>> listTodaysVisit() {
    return get("list-todays-visit", setter -> {}, new GenericType<List<VisitPatientDTO>>() {});
  }

  public CompletableFuture<VisitFull2PageDTO> listVisitFull2(int patientId, int page) {
    return get(
        "list-visit-full2",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("page", page);
        },
        new GenericType<VisitFull2PageDTO>() {});
  }

  public CompletableFuture<VisitFullDTO> getVisitFull(int visitId) {
    return get(
        "get-visit-full",
        setter -> setter.set("visit-id", visitId),
        new GenericType<VisitFullDTO>() {});
  }

  public CompletableFuture<VisitFull2DTO> getVisitFull2(int visitId) {
    return get(
        "get-visit-full2",
        setter -> setter.set("visit-id", visitId),
        new GenericType<VisitFull2DTO>() {});
  }

  public CompletableFuture<List<ShoukiDTO>> batchGetShouki(List<Integer> visitIds) {
    return post(
        "batch-get-shouki",
        setter -> {},
        Entity.entity(visitIds, MediaType.APPLICATION_JSON),
        new GenericType<List<ShoukiDTO>>() {});
  }

  public CompletableFuture<Void> updateShouki(ShoukiDTO shouki) {
    return post(
        "update-shouki",
        setter -> {},
        Entity.entity(shouki, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteShouki(int visitId) {
    return post(
        "delete-shouki",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Integer> enterText(TextDTO text) {
    return post(
        "enter-text",
        setter -> {},
        Entity.entity(text, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<TextDTO> getText(int textId) {
    return get("get-text", setter -> setter.set("text-id", textId), new GenericType<TextDTO>() {});
  }

  public CompletableFuture<Void> updateText(TextDTO text) {
    return post(
        "update-text",
        setter -> {},
        Entity.entity(text, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteText(int textId) {
    return post(
        "delete-text",
        setter -> setter.set("text-id", textId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<TextDTO>> listText(int visitId) {
    return get(
        "list-text",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<TextDTO>>() {});
  }

  public CompletableFuture<TextVisitPageDTO> searchText(int patientId, String text, int page) {
    return get(
        "search-text",
        setter -> {
          setter.set("patient-id", patientId);
          setter.set("text", text);
          setter.set("page", page);
        },
        new GenericType<TextVisitPageDTO>() {});
  }

  public CompletableFuture<TextVisitPatientPageDTO> searchTextGlobally(String text, int page) {
    return get(
        "search-text-globally",
        setter -> {
          setter.set("text", text);
          setter.set("page", page);
        },
        new GenericType<TextVisitPatientPageDTO>() {});
  }

  public CompletableFuture<ShinryouDTO> getShinryou(int shinryouId) {
    return get(
        "get-shinryou",
        setter -> setter.set("shinryou-id", shinryouId),
        new GenericType<ShinryouDTO>() {});
  }

  public CompletableFuture<ShinryouWithAttrDTO> getShinryouWithAttr(int shinryouId) {
    return get(
        "get-shinryou-with-attr",
        setter -> setter.set("shinryou-id", shinryouId),
        new GenericType<ShinryouWithAttrDTO>() {});
  }

  public CompletableFuture<Integer> enterShinryou(ShinryouDTO shinryou) {
    return post(
        "enter-shinryou",
        setter -> {},
        Entity.entity(shinryou, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<ShinryouDTO> enterShinryouByName(int visitId, String name) {
    return post(
        "enter-shinryou-by-name",
        setter -> {
          setter.set("visit-id", visitId);
          setter.set("name", name);
        },
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<ShinryouDTO>() {});
  }

  public CompletableFuture<Void> deleteShinryou(int shinryouId) {
    return post(
        "delete-shinryou",
        setter -> setter.set("shinryou-id", shinryouId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteShinryouCascading(int shinryouId) {
    return post(
        "delete-shinryou-cascading",
        setter -> setter.set("shinryou-id", shinryouId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> batchDeleteShinryouCascading(List<Integer> shinryouIds) {
    return post(
        "batch-delete-shinryou-cascading",
        setter -> {},
        Entity.entity(shinryouIds, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<ShinryouFullDTO> getShinryouFull(int shinryouId) {
    return get(
        "get-shinryou-full",
        setter -> setter.set("shinryou-id", shinryouId),
        new GenericType<ShinryouFullDTO>() {});
  }

  public CompletableFuture<ShinryouFullWithAttrDTO> getShinryouFullWithAttr(int shinryouId) {
    return get(
        "get-shinryou-full-with-attr",
        setter -> setter.set("shinryou-id", shinryouId),
        new GenericType<ShinryouFullWithAttrDTO>() {});
  }

  public CompletableFuture<Void> batchEnterShinryou(List<ShinryouDTO> shinryouList) {
    return post(
        "batch-enter-shinryou",
        setter -> {},
        Entity.entity(shinryouList, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<ShinryouFullDTO>> listShinryouFullByIds(List<Integer> shinryouIds) {
    return post(
        "list-shinryou-full-by-ids",
        setter -> {},
        Entity.entity(shinryouIds, MediaType.APPLICATION_JSON),
        new GenericType<List<ShinryouFullDTO>>() {});
  }

  public CompletableFuture<List<ShinryouFullWithAttrDTO>> listShinryouFullWithAttrByIds(
      List<Integer> shinryouIds) {
    return post(
        "list-shinryou-full-with-attr-by-ids",
        setter -> {},
        Entity.entity(shinryouIds, MediaType.APPLICATION_JSON),
        new GenericType<List<ShinryouFullWithAttrDTO>>() {});
  }

  public CompletableFuture<List<ShinryouDTO>> listShinryou(int visitId) {
    return get(
        "list-shinryou",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ShinryouDTO>>() {});
  }

  public CompletableFuture<List<ShinryouWithAttrDTO>> listShinryouWithAttr(int visitId) {
    return get(
        "list-shinryou-with-attr",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ShinryouWithAttrDTO>>() {});
  }

  public CompletableFuture<List<ShinryouFullDTO>> listShinryouFull(int visitId) {
    return get(
        "list-shinryou-full",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ShinryouFullDTO>>() {});
  }

  public CompletableFuture<List<ShinryouFullWithAttrDTO>> listShinryouFullWithAttr(int visitId) {
    return get(
        "list-shinryou-full-with-attr",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ShinryouFullWithAttrDTO>>() {});
  }

  public CompletableFuture<List<Integer>> deleteDuplicateShinryou(int visitId) {
    return post(
        "delete-duplicate-shinryou",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<List<Integer>>() {});
  }

  public CompletableFuture<List<ShinryouAttrDTO>> batchGetShinryouAttr(List<Integer> shinryouIds) {
    return post(
        "batch-get-shinryou-attr",
        setter -> {},
        Entity.entity(shinryouIds, MediaType.APPLICATION_JSON),
        new GenericType<List<ShinryouAttrDTO>>() {});
  }

  public CompletableFuture<ShinryouAttrDTO> getShinryouAttr(int shinryouId) {
    return get(
        "get-shinryou-attr",
        setter -> setter.set("shinryou-id", shinryouId),
        new GenericType<ShinryouAttrDTO>() {});
  }

  public CompletableFuture<Void> enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
    return post(
        "enter-shinryou-attr",
        setter -> {},
        Entity.entity(shinryouAttr, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> setShinryouAttr(int shinryouId, ShinryouAttrDTO attr) {
    return post(
        "set-shinryou-attr",
        setter -> setter.set("shinryou-id", shinryouId),
        Entity.entity(attr, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Integer> enterConduct(ConductDTO conduct) {
    return post(
        "enter-conduct",
        setter -> {},
        Entity.entity(conduct, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<ConductFullDTO> enterConductFull(ConductEnterRequestDTO req) {
    return post(
        "enter-conduct-full",
        setter -> {},
        Entity.entity(req, MediaType.APPLICATION_JSON),
        new GenericType<ConductFullDTO>() {});
  }

  public CompletableFuture<ConductDTO> getConduct(int conductId) {
    return get(
        "get-conduct",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<ConductDTO>() {});
  }

  public CompletableFuture<Void> deleteConductCascading(int conductId) {
    return post(
        "delete-conduct-cascading",
        setter -> setter.set("conduct-id", conductId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> modifyConductKind(int conductId, int conductKind) {
    return post(
        "modify-conduct-kind",
        setter -> {
          setter.set("conduct-id", conductId);
          setter.set("conduct-kind", conductKind);
        },
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<ConductDTO>> listConduct(int visitId) {
    return get(
        "list-conduct",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ConductDTO>>() {});
  }

  public CompletableFuture<List<ConductFullDTO>> listConductFullByIds(List<Integer> conductIds) {
    return post(
        "list-conduct-full-by-ids",
        setter -> {},
        Entity.entity(conductIds, MediaType.APPLICATION_JSON),
        new GenericType<List<ConductFullDTO>>() {});
  }

  public CompletableFuture<ConductFullDTO> getConductFull(int conductId) {
    return get(
        "get-conduct-full",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<ConductFullDTO>() {});
  }

  public CompletableFuture<List<ConductFullDTO>> listConductFull(int visitId) {
    return get(
        "list-conduct-full",
        setter -> setter.set("visit-id", visitId),
        new GenericType<List<ConductFullDTO>>() {});
  }

  public CompletableFuture<Void> enterGazouLabel(GazouLabelDTO gazouLabel) {
    return post(
        "enter-gazou-label",
        setter -> {},
        Entity.entity(gazouLabel, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<GazouLabelDTO> getGazouLabel(int conductId) {
    return get(
        "get-gazou-label",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<GazouLabelDTO>() {});
  }

  public CompletableFuture<Void> deleteGazouLabel(int conductId) {
    return post(
        "delete-gazou-label",
        setter -> setter.set("conduct-id", conductId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> updateGazouLabel(GazouLabelDTO gazouLabel) {
    return post(
        "update-gazou-label",
        setter -> {},
        Entity.entity(gazouLabel, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> modifyGazouLabel(int conductId, String label) {
    return post(
        "modify-gazou-label",
        setter -> {
          setter.set("conduct-id", conductId);
          setter.set("label", label);
        },
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Integer> enterConductShinryou(ConductShinryouDTO shinryou) {
    return post(
        "enter-conduct-shinryou",
        setter -> {},
        Entity.entity(shinryou, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<ConductShinryouDTO> getConductShinryou(int conductShinryouId) {
    return get(
        "get-conduct-shinryou",
        setter -> setter.set("conduct-shinryou-id", conductShinryouId),
        new GenericType<ConductShinryouDTO>() {});
  }

  public CompletableFuture<Void> deleteConductShinryou(int conductShinryouId) {
    return post(
        "delete-conduct-shinryou",
        setter -> setter.set("conduct-shinryou-id", conductShinryouId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<ConductShinryouDTO>> listConductShinryou(int conductId) {
    return get(
        "list-conduct-shinryou",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductShinryouDTO>>() {});
  }

  public CompletableFuture<ConductShinryouFullDTO> getConductShinryouFull(int conductShinryouId) {
    return get(
        "get-conduct-shinryou-full",
        setter -> setter.set("conduct-shinryou-id", conductShinryouId),
        new GenericType<ConductShinryouFullDTO>() {});
  }

  public CompletableFuture<List<ConductShinryouFullDTO>> listConductShinryouFull(int conductId) {
    return get(
        "list-conduct-shinryou-full",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductShinryouFullDTO>>() {});
  }

  public CompletableFuture<Integer> enterConductDrug(ConductDrugDTO drug) {
    return post(
        "enter-conduct-drug",
        setter -> {},
        Entity.entity(drug, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<ConductDrugDTO> getConductDrug(int conductDrugId) {
    return get(
        "get-conduct-drug",
        setter -> setter.set("conduct-drug-id", conductDrugId),
        new GenericType<ConductDrugDTO>() {});
  }

  public CompletableFuture<Void> deleteConductDrug(int conductDrugId) {
    return post(
        "delete-conduct-drug",
        setter -> setter.set("conduct-drug-id", conductDrugId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<ConductDrugDTO>> listConductDrug(int conductId) {
    return get(
        "list-conduct-drug",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductDrugDTO>>() {});
  }

  public CompletableFuture<ConductDrugFullDTO> getConductDrugFull(int conductDrugId) {
    return get(
        "get-conduct-drug-full",
        setter -> setter.set("conduct-drug-id", conductDrugId),
        new GenericType<ConductDrugFullDTO>() {});
  }

  public CompletableFuture<List<ConductDrugFullDTO>> listConductDrugFull(int conductId) {
    return get(
        "list-conduct-drug-full",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductDrugFullDTO>>() {});
  }

  public CompletableFuture<Integer> enterConductKizai(ConductKizaiDTO kizai) {
    return post(
        "enter-conduct-kizai",
        setter -> {},
        Entity.entity(kizai, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<ConductKizaiDTO> getConductKizai(int conductKizaiId) {
    return get(
        "get-conduct-kizai",
        setter -> setter.set("conduct-kizai-id", conductKizaiId),
        new GenericType<ConductKizaiDTO>() {});
  }

  public CompletableFuture<Void> deleteConductKizai(int conductKizaiId) {
    return post(
        "delete-conduct-kizai",
        setter -> setter.set("conduct-kizai-id", conductKizaiId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<ConductKizaiDTO>> listConductKizai(int conductId) {
    return get(
        "list-conduct-kizai",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductKizaiDTO>>() {});
  }

  public CompletableFuture<ConductKizaiFullDTO> getConductKizaiFull(int conductKizaiId) {
    return get(
        "get-conduct-kizai-full",
        setter -> setter.set("conduct-kizai-id", conductKizaiId),
        new GenericType<ConductKizaiFullDTO>() {});
  }

  public CompletableFuture<List<ConductKizaiFullDTO>> listConductKizaiFull(int conductId) {
    return get(
        "list-conduct-kizai-full",
        setter -> setter.set("conduct-id", conductId),
        new GenericType<List<ConductKizaiFullDTO>>() {});
  }

  public CompletableFuture<Void> finishCashier(PaymentDTO payment) {
    return post(
        "finish-cashier",
        setter -> {},
        Entity.entity(payment, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<ShahokokuhoDTO> getShahokokuho(int shahokokuhoId) {
    return get(
        "get-shahokokuho",
        setter -> setter.set("shahokokuho-id", shahokokuhoId),
        new GenericType<ShahokokuhoDTO>() {});
  }

  public CompletableFuture<Integer> enterShahokokuho(ShahokokuhoDTO shahokokuho) {
    return post(
        "enter-shahokokuho",
        setter -> {},
        Entity.entity(shahokokuho, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<KoukikoureiDTO> getKoukikourei(int koukikoureiId) {
    return get(
        "get-koukikourei",
        setter -> setter.set("koukikourei-id", koukikoureiId),
        new GenericType<KoukikoureiDTO>() {});
  }

  public CompletableFuture<RoujinDTO> getRoujin(int roujinId) {
    return get(
        "get-roujin", setter -> setter.set("roujin-id", roujinId), new GenericType<RoujinDTO>() {});
  }

  public CompletableFuture<KouhiDTO> getKouhi(int kouhiId) {
    return get(
        "get-kouhi", setter -> setter.set("kouhi-id", kouhiId), new GenericType<KouhiDTO>() {});
  }

  public CompletableFuture<Integer> enterDisease(DiseaseDTO disease) {
    return post(
        "enter-disease",
        setter -> {},
        Entity.entity(disease, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<Void> enterNewDisease(DiseaseNewDTO disease) {
    return post(
        "enter-new-disease",
        setter -> {},
        Entity.entity(disease, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<DiseaseDTO> getDisease(int diseaseId) {
    return get(
        "get-disease",
        setter -> setter.set("disease-id", diseaseId),
        new GenericType<DiseaseDTO>() {});
  }

  public CompletableFuture<Void> updateDisease(DiseaseDTO disease) {
    return post(
        "update-disease",
        setter -> {},
        Entity.entity(disease, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> deleteDisease(int diseaseId) {
    return post(
        "delete-disease",
        setter -> setter.set("disease-id", diseaseId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<DiseaseFullDTO> getDiseaseFull(int diseaseId) {
    return get(
        "get-disease-full",
        setter -> setter.set("disease-id", diseaseId),
        new GenericType<DiseaseFullDTO>() {});
  }

  public CompletableFuture<List<DiseaseFullDTO>> listCurrentDiseaseFull(int patientId) {
    return get(
        "list-current-disease-full",
        setter -> setter.set("patient-id", patientId),
        new GenericType<List<DiseaseFullDTO>>() {});
  }

  public CompletableFuture<List<DiseaseFullDTO>> listDiseaseFull(int patientId) {
    return get(
        "list-disease-full",
        setter -> setter.set("patient-id", patientId),
        new GenericType<List<DiseaseFullDTO>>() {});
  }

  public CompletableFuture<Void> batchUpdateDiseaseEndReason(
      List<DiseaseModifyEndReasonDTO> modifications) {
    return post(
        "batch-update-disease-end-reason",
        setter -> {},
        Entity.entity(modifications, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> modifyDisease(DiseaseModifyDTO diseaseModifyDTO) {
    return post(
        "modify-disease",
        setter -> {},
        Entity.entity(diseaseModifyDTO, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<PharmaQueueDTO> getPharmaQueue(int visitId) {
    return get(
        "get-pharma-queue",
        setter -> setter.set("visit-id", visitId),
        new GenericType<PharmaQueueDTO>() {});
  }

  public CompletableFuture<Void> deletePharmaQueue(int visitId) {
    return post(
        "delete-pharma-queue",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<ShinryouMasterDTO> findShinryouMasterByName(String name, LocalDate at) {
    return get(
        "find-shinryou-master-by-name",
        setter -> {
          setter.set("name", name);
          setter.set("at", at);
        },
        new GenericType<ShinryouMasterDTO>() {});
  }

  public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByName(
      List<String> nameCandidates, LocalDate at) {
    return post(
        "resolve-shinryou-master-by-name",
        setter -> setter.set("at", at),
        Entity.entity(nameCandidates, MediaType.APPLICATION_JSON),
        new GenericType<ShinryouMasterDTO>() {});
  }

  public CompletableFuture<ShinryouMasterDTO> resolveShinryouMasterByKey(String key, LocalDate at) {
    return get(
        "resolve-shinryou-master-by-key",
        setter -> {
          setter.set("key", key);
          setter.set("at", at);
        },
        new GenericType<ShinryouMasterDTO>() {});
  }

  public CompletableFuture<Map<String, Integer>> batchResolveShinryouNames(
      List<List<String>> args, LocalDate at) {
    return post(
        "batch-resolve-shinryou-names",
        setter -> setter.set("at", at),
        Entity.entity(args, MediaType.APPLICATION_JSON),
        new GenericType<Map<String, Integer>>() {});
  }

  public CompletableFuture<List<ShinryouMasterDTO>> searchShinryouMaster(
      String text, LocalDate at) {
    return get(
        "search-shinryou-master",
        setter -> {
          setter.set("text", text);
          setter.set("at", at);
        },
        new GenericType<List<ShinryouMasterDTO>>() {});
  }

  public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, LocalDate at) {
    return get(
        "get-shinryou-master",
        setter -> {
          setter.set("shinryoucode", shinryoucode);
          setter.set("at", at);
        },
        new GenericType<ShinryouMasterDTO>() {});
  }

  public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode, LocalDate at) {
    return get(
        "get-iyakuhin-master",
        setter -> {
          setter.set("iyakuhincode", iyakuhincode);
          setter.set("at", at);
        },
        new GenericType<IyakuhinMasterDTO>() {});
  }

  public CompletableFuture<List<IyakuhinMasterDTO>> searchIyakuhinMaster(
      String text, LocalDate at) {
    return get(
        "search-iyakuhin-master",
        setter -> {
          setter.set("text", text);
          setter.set("at", at);
        },
        new GenericType<List<IyakuhinMasterDTO>>() {});
  }

  public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode, LocalDate at) {
    return get(
        "get-kizai-master",
        setter -> {
          setter.set("kizaicode", kizaicode);
          setter.set("at", at);
        },
        new GenericType<KizaiMasterDTO>() {});
  }

  public CompletableFuture<KizaiMasterDTO> findKizaiMasterByName(String name, LocalDate at) {
    return get(
        "find-kizai-master-by-name",
        setter -> {
          setter.set("name", name);
          setter.set("at", at);
        },
        new GenericType<KizaiMasterDTO>() {});
  }

  public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByName(
      List<String> nameCandidates, LocalDate at) {
    return post(
        "resolve-kizai-master-by-name",
        setter -> setter.set("at", at),
        Entity.entity(nameCandidates, MediaType.APPLICATION_JSON),
        new GenericType<KizaiMasterDTO>() {});
  }

  public CompletableFuture<KizaiMasterDTO> resolveKizaiMasterByKey(String key, LocalDate at) {
    return get(
        "resolve-kizai-master-by-key",
        setter -> {
          setter.set("key", key);
          setter.set("at", at);
        },
        new GenericType<KizaiMasterDTO>() {});
  }

  public CompletableFuture<Map<String, Integer>> batchResolveKizaiNames(
      List<List<String>> args, LocalDate at) {
    return post(
        "batch-resolve-kizai-names",
        setter -> setter.set("at", at),
        Entity.entity(args, MediaType.APPLICATION_JSON),
        new GenericType<Map<String, Integer>>() {});
  }

  public CompletableFuture<List<KizaiMasterDTO>> searchKizaiMaster(String text, LocalDate at) {
    return get(
        "search-kizai-master",
        setter -> {
          setter.set("text", text);
          setter.set("at", at);
        },
        new GenericType<List<KizaiMasterDTO>>() {});
  }

  public CompletableFuture<List<ByoumeiMasterDTO>> searchByoumeiMaster(String text, LocalDate at) {
    return get(
        "search-byoumei-master",
        setter -> {
          setter.set("text", text);
          setter.set("at", at);
        },
        new GenericType<List<ByoumeiMasterDTO>>() {});
  }

  public CompletableFuture<ByoumeiMasterDTO> getByoumeiMasterByName(String name, LocalDate at) {
    return get(
        "get-byoumei-master-by-name",
        setter -> {
          setter.set("name", name);
          setter.set("at", at);
        },
        new GenericType<ByoumeiMasterDTO>() {});
  }

  public CompletableFuture<List<ShuushokugoMasterDTO>> searchShuushokugoMaster(
      String text, LocalDate at) {
    return get(
        "search-shuushokugo-master",
        setter -> {
          setter.set("text", text);
          setter.set("at", at);
        },
        new GenericType<List<ShuushokugoMasterDTO>>() {});
  }

  public CompletableFuture<ShuushokugoMasterDTO> getShuushokugoMasterByName(String name) {
    return get(
        "get-shuushokugo-master-by-name",
        setter -> setter.set("name", name),
        new GenericType<ShuushokugoMasterDTO>() {});
  }

  public CompletableFuture<Integer> enterPrescExample(PrescExampleDTO prescExample) {
    return post(
        "enter-presc-example",
        setter -> {},
        Entity.entity(prescExample, MediaType.APPLICATION_JSON),
        new GenericType<Integer>() {});
  }

  public CompletableFuture<Void> deletePrescExample(int prescExampleId) {
    return post(
        "delete-presc-example",
        setter -> setter.set("presc-example-id", prescExampleId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<Void> updatePrescExample(PrescExampleDTO prescExample) {
    return post(
        "update-presc-example",
        setter -> {},
        Entity.entity(prescExample, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<PrescExampleFullDTO>> searchPrescExample(String text) {
    return get(
        "search-presc-example",
        setter -> setter.set("text", text),
        new GenericType<List<PrescExampleFullDTO>>() {});
  }

  public CompletableFuture<List<PrescExampleFullDTO>> listAllPrescExample() {
    return get(
        "list-all-presc-example", setter -> {}, new GenericType<List<PrescExampleFullDTO>>() {});
  }

  public CompletableFuture<BatchEnterResultDTO> batchEnter(BatchEnterRequestDTO req) {
    return post(
        "batch-enter",
        setter -> {},
        Entity.entity(req, MediaType.APPLICATION_JSON),
        new GenericType<BatchEnterResultDTO>() {});
  }

  public CompletableFuture<BatchEnterResultDTO> batchEnterByNames(
      int visitId, BatchEnterByNamesRequestDTO req) {
    return post(
        "batch-enter-by-names",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(req, MediaType.APPLICATION_JSON),
        new GenericType<BatchEnterResultDTO>() {});
  }

  public CompletableFuture<Void> prescDone(int visitId) {
    return post(
        "presc-done",
        setter -> setter.set("visit-id", visitId),
        Entity.entity(null, MediaType.APPLICATION_JSON),
        new GenericType<Void>() {});
  }

  public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
    return get("list-disease-example", setter -> {}, new GenericType<List<DiseaseExampleDTO>>() {});
  }

  public CompletableFuture<MeisaiDTO> getMeisai(int visitId) {
    return get(
        "get-meisai", setter -> setter.set("visit-id", visitId), new GenericType<MeisaiDTO>() {});
  }

  public CompletableFuture<IyakuhinMasterDTO> resolveStockDrug(int iyakuhincode, LocalDate at) {
    return get(
        "resolve-stock-drug",
        setter -> {
          setter.set("iyakuhincode", iyakuhincode);
          setter.set("at", at);
        },
        new GenericType<IyakuhinMasterDTO>() {});
  }

  public CompletableFuture<List<ResolvedStockDrugDTO>> batchResolveStockDrug(
      List<Integer> iyakuhincodes, LocalDate at) {
    return post(
        "batch-resolve-stock-drug",
        setter -> setter.set("at", at),
        Entity.entity(iyakuhincodes, MediaType.APPLICATION_JSON),
        new GenericType<List<ResolvedStockDrugDTO>>() {});
  }

  public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
    return get("get-clinic-info", setter -> {}, new GenericType<ClinicInfoDTO>() {});
  }

  public CompletableFuture<PracticeLogDTO> getLastPracticeLog() {
    return get("get-last-practice-log", setter -> {}, new GenericType<PracticeLogDTO>() {});
  }

  public CompletableFuture<Integer> getLastPracticeLogId() {
    return get("get-last-practice-log-id", setter -> {}, new GenericType<Integer>() {});
  }

  public CompletableFuture<List<PracticeLogDTO>> listPracticeLogSince(int afterThisId) {
    return get(
        "list-practice-log-since",
        setter -> setter.set("after-this-id", afterThisId),
        new GenericType<List<PracticeLogDTO>>() {});
  }
}
