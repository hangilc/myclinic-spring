package jp.chang.myclinic.backendserver;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;

@Path("/api")
public class RestServer {

    private DbBackend dbBackend;

    @Inject
    RestServer(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
    }

    @Path("enter-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterPatient(PatientDTO patient) {
        return dbBackend.tx(backend -> {
            backend.enterPatient(patient);
            return patient.patientId;
        });
    }

    @Path("get-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public PatientDTO getPatient(@QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.getPatient(patientId));
    }

    @Path("update-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updatePatient(PatientDTO patient) {
        dbBackend.txProc(backend -> backend.updatePatient(patient));
    }

    @Path("search-patient-by-keyword2")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PatientDTO> searchPatientByKeyword2(@QueryParam("last-name-keyword") String lastNameKeyword, @QueryParam("first-name-keyword") String firstNameKeyword) {
        return dbBackend.query(backend -> backend.searchPatientByKeyword2(lastNameKeyword, firstNameKeyword));
    }

    @Path("search-patient-by-keyword")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PatientDTO> searchPatientByKeyword(@QueryParam("keyword") String keyword) {
        return dbBackend.query(backend -> backend.searchPatientByKeyword(keyword));
    }

    @Path("search-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PatientDTO> searchPatient(@QueryParam("text") String text) {
        return dbBackend.query(backend -> backend.searchPatient(text));
    }

    @Path("get-visit")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public VisitDTO getVisit(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getVisit(visitId));
    }

    @Path("list-recent-visit-with-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<VisitPatientDTO> listRecentVisitWithPatient(@QueryParam("page") int page, @QueryParam("items-per-page") int itemsPerPage) {
        return dbBackend.query(backend -> backend.listRecentVisitWithPatient(page, itemsPerPage));
    }

    @Path("list-todays-visit")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<VisitPatientDTO> listTodaysVisit() {
        return dbBackend.query(backend -> backend.listTodaysVisit());
    }

    @Path("list-visit-full2")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public VisitFull2PageDTO listVisitFull2(@QueryParam("patient-id") int patientId, @QueryParam("page") int page) {
        return dbBackend.query(backend -> backend.listVisitFull2(patientId, page));
    }

    @Path("get-visit-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public VisitFullDTO getVisitFull(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getVisitFull(visitId));
    }

    @Path("get-visit-full2")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public VisitFull2DTO getVisitFull2(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getVisitFull2(visitId));
    }

    @Path("get-charge")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ChargeDTO getCharge(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getCharge(visitId));
    }

    @Path("list-payment")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PaymentDTO> listPayment(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listPayment(visitId));
    }

    @Path("get-wqueue")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public WqueueDTO getWqueue(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getWqueue(visitId));
    }

    @Path("list-wqueue")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<WqueueDTO> listWqueue() {
        return dbBackend.query(backend -> backend.listWqueue());
    }

    @Path("list-wqueue-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<WqueueFullDTO> listWqueueFull() {
        return dbBackend.query(backend -> backend.listWqueueFull());
    }

    @Path("list-wqueue-full-for-exam")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<WqueueFullDTO> listWqueueFullForExam() {
        return dbBackend.query(backend -> backend.listWqueueFullForExam());
    }

    @Path("get-hoken")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public HokenDTO getHoken(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getHoken(visitId));
    }

    @Path("list-available-hoken")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public HokenDTO listAvailableHoken(@QueryParam("patient-id") int patientId, @QueryParam("visited-at") LocalDate visitedAt) {
        return dbBackend.query(backend -> backend.listAvailableHoken(patientId, visitedAt));
    }

    @Path("update-hoken")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateHoken(VisitDTO visit) {
        dbBackend.txProc(backend -> backend.updateHoken(visit));
    }

    @Path("get-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DrugDTO getDrug(@QueryParam("drug-id") int drugId) {
        return dbBackend.query(backend -> backend.getDrug(drugId));
    }

    @Path("get-drug-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DrugWithAttrDTO getDrugWithAttr(@QueryParam("drug-id") int drugId) {
        return dbBackend.query(backend -> backend.getDrugWithAttr(drugId));
    }

    @Path("batch-update-drug-days")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int batchUpdateDrugDays(List<Integer> drugIds, @QueryParam("days") int days) {
        return dbBackend.tx(backend -> backend.batchUpdateDrugDays(drugIds, days));
    }

    @Path("mark-drugs-as-prescribed")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public int markDrugsAsPrescribed(@QueryParam("visit-id") int visitId) {
        return dbBackend.tx(backend -> backend.markDrugsAsPrescribed(visitId));
    }

    @Path("get-drug-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DrugFullDTO getDrugFull(@QueryParam("drug-id") int drugId) {
        return dbBackend.query(backend -> backend.getDrugFull(drugId));
    }

    @Path("get-drug-full-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DrugFullWithAttrDTO getDrugFullWithAttr(@QueryParam("drug-id") int drugId) {
        return dbBackend.query(backend -> backend.getDrugFullWithAttr(drugId));
    }

    @Path("list-drug-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DrugWithAttrDTO> listDrugWithAttr(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listDrugWithAttr(visitId));
    }

    @Path("list-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DrugDTO> listDrug(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listDrug(visitId));
    }

    @Path("list-drug-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DrugFullDTO> listDrugFull(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listDrugFull(visitId));
    }

    @Path("list-representative-gaiyou-drug-id")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<Integer> listRepresentativeGaiyouDrugId(@QueryParam("text") String text, @QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.listRepresentativeGaiyouDrugId(text, patientId));
    }

    @Path("list-prev-drug-by-patient")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DrugFullDTO> listPrevDrugByPatient(@QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.listPrevDrugByPatient(patientId));
    }

    @Path("search-prev-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DrugFullDTO> searchPrevDrug(@QueryParam("text") String text, @QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.searchPrevDrug(text, patientId));
    }

    @Path("count-unprescribed-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public int countUnprescribedDrug(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.countUnprescribedDrug(visitId));
    }

    @Path("get-drug-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DrugAttrDTO getDrugAttr(@QueryParam("drug-id") int drugId) {
        return dbBackend.query(backend -> backend.getDrugAttr(drugId));
    }

    @Path("enter-drug-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void enterDrugAttr(DrugAttrDTO drugAttr) {
        dbBackend.txProc(backend -> backend.enterDrugAttr(drugAttr));
    }

    @Path("update-drug-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateDrugAttr(DrugAttrDTO drugAttr) {
        dbBackend.txProc(backend -> backend.updateDrugAttr(drugAttr));
    }

    @Path("delete-drug-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteDrugAttr(@QueryParam("drug-id") int drugId) {
        dbBackend.txProc(backend -> backend.deleteDrugAttr(drugId));
    }

    @Path("batch-get-drug-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return dbBackend.tx(backend -> backend.batchGetDrugAttr(drugIds));
    }

    @Path("batch-get-shouki")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return dbBackend.tx(backend -> backend.batchGetShouki(visitIds));
    }

    @Path("update-shouki")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateShouki(ShoukiDTO shouki) {
        dbBackend.txProc(backend -> backend.updateShouki(shouki));
    }

    @Path("delete-shouki")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteShouki(@QueryParam("visit-id") int visitId) {
        dbBackend.txProc(backend -> backend.deleteShouki(visitId));
    }

    @Path("enter-text")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterText(TextDTO text) {
        return dbBackend.tx(backend -> {
            backend.enterText(text);
            return text.textId;
        });
    }

    @Path("get-text")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TextDTO getText(@QueryParam("text-id") int textId) {
        return dbBackend.query(backend -> backend.getText(textId));
    }

    @Path("update-text")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateText(TextDTO text) {
        dbBackend.txProc(backend -> backend.updateText(text));
    }

    @Path("delete-text")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteText(@QueryParam("text-id") int textId) {
        dbBackend.txProc(backend -> backend.deleteText(textId));
    }

    @Path("list-text")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<TextDTO> listText(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listText(visitId));
    }

    @Path("search-text")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TextVisitPageDTO searchText(@QueryParam("patient-id") int patientId, @QueryParam("text") String text, @QueryParam("page") int page) {
        return dbBackend.query(backend -> backend.searchText(patientId, text, page));
    }

    @Path("search-text-globally")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TextVisitPatientPageDTO searchTextGlobally(@QueryParam("text") String text, @QueryParam("page") int page) {
        return dbBackend.query(backend -> backend.searchTextGlobally(text, page));
    }

    @Path("get-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouDTO getShinryou(@QueryParam("shinryou-id") int shinryouId) {
        return dbBackend.query(backend -> backend.getShinryou(shinryouId));
    }

    @Path("get-shinryou-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouWithAttrDTO getShinryouWithAttr(@QueryParam("shinryou-id") int shinryouId) {
        return dbBackend.query(backend -> backend.getShinryouWithAttr(shinryouId));
    }

    @Path("get-shinryou-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouFullDTO getShinryouFull(@QueryParam("shinryou-id") int shinryouId) {
        return dbBackend.query(backend -> backend.getShinryouFull(shinryouId));
    }

    @Path("get-shinryou-full-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouFullWithAttrDTO getShinryouFullWithAttr(@QueryParam("shinryou-id") int shinryouId) {
        return dbBackend.query(backend -> backend.getShinryouFullWithAttr(shinryouId));
    }

    @Path("list-shinryou-full-by-ids")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<ShinryouFullDTO> listShinryouFullByIds(List<Integer> shinryouIds) {
        return dbBackend.query(backend -> backend.listShinryouFullByIds(shinryouIds));
    }

    @Path("list-shinryou-full-with-attr-by-ids")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<ShinryouFullWithAttrDTO> listShinryouFullWithAttrByIds(List<Integer> shinryouIds) {
        return dbBackend.query(backend -> backend.listShinryouFullWithAttrByIds(shinryouIds));
    }

    @Path("list-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShinryouDTO> listShinryou(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listShinryou(visitId));
    }

    @Path("list-shinryou-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShinryouWithAttrDTO> listShinryouWithAttr(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listShinryouWithAttr(visitId));
    }

    @Path("list-shinryou-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShinryouFullDTO> listShinryouFull(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listShinryouFull(visitId));
    }

    @Path("list-shinryou-full-with-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShinryouFullWithAttrDTO> listShinryouFullWithAttr(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listShinryouFullWithAttr(visitId));
    }

    @Path("batch-get-shinryou-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return dbBackend.tx(backend -> backend.batchGetShinryouAttr(shinryouIds));
    }

    @Path("get-shinryou-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouAttrDTO getShinryouAttr(@QueryParam("shinryou-id") int shinryouId) {
        return dbBackend.query(backend -> backend.getShinryouAttr(shinryouId));
    }

    @Path("enter-shinryou-attr")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void enterShinryouAttr(ShinryouAttrDTO shinryouAttr) {
        dbBackend.txProc(backend -> backend.enterShinryouAttr(shinryouAttr));
    }

    @Path("enter-conduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterConduct(ConductDTO conduct) {
        return dbBackend.tx(backend -> {
            backend.enterConduct(conduct);
            return conduct.conductId;
        });
    }

    @Path("get-conduct")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductDTO getConduct(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.getConduct(conductId));
    }

    @Path("modify-conduct-kind")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void modifyConductKind(@QueryParam("conduct-id") int conductId, @QueryParam("conduct-kind") int conductKind) {
        dbBackend.txProc(backend -> backend.modifyConductKind(conductId, conductKind));
    }

    @Path("list-conduct")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductDTO> listConduct(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listConduct(visitId));
    }

    @Path("list-conduct-full-by-ids")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public List<ConductFullDTO> listConductFullByIds(List<Integer> conductIds) {
        return dbBackend.query(backend -> backend.listConductFullByIds(conductIds));
    }

    @Path("get-conduct-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductFullDTO getConductFull(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.getConductFull(conductId));
    }

    @Path("list-conduct-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductFullDTO> listConductFull(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.listConductFull(visitId));
    }

    @Path("enter-gazou-label")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void enterGazouLabel(GazouLabelDTO gazouLabel) {
        dbBackend.txProc(backend -> backend.enterGazouLabel(gazouLabel));
    }

    @Path("get-gazou-label")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public GazouLabelDTO getGazouLabel(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.getGazouLabel(conductId));
    }

    @Path("delete-gazou-label")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteGazouLabel(@QueryParam("conduct-id") int conductId) {
        dbBackend.txProc(backend -> backend.deleteGazouLabel(conductId));
    }

    @Path("update-gazou-label")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateGazouLabel(GazouLabelDTO gazouLabel) {
        dbBackend.txProc(backend -> backend.updateGazouLabel(gazouLabel));
    }

    @Path("enter-conduct-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterConductShinryou(ConductShinryouDTO shinryou) {
        return dbBackend.tx(backend -> {
            backend.enterConductShinryou(shinryou);
            return shinryou.conductShinryouId;
        });
    }

    @Path("get-conduct-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductShinryouDTO getConductShinryou(@QueryParam("conduct-shinryou-id") int conductShinryouId) {
        return dbBackend.query(backend -> backend.getConductShinryou(conductShinryouId));
    }

    @Path("delete-conduct-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteConductShinryou(@QueryParam("conduct-shinryou-id") int conductShinryouId) {
        dbBackend.txProc(backend -> backend.deleteConductShinryou(conductShinryouId));
    }

    @Path("list-conduct-shinryou")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductShinryouDTO> listConductShinryou(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductShinryou(conductId));
    }

    @Path("get-conduct-shinryou-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductShinryouFullDTO getConductShinryouFull(@QueryParam("conduct-shinryou-id") int conductShinryouId) {
        return dbBackend.query(backend -> backend.getConductShinryouFull(conductShinryouId));
    }

    @Path("list-conduct-shinryou-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductShinryouFullDTO> listConductShinryouFull(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductShinryouFull(conductId));
    }

    @Path("enter-conduct-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterConductDrug(ConductDrugDTO drug) {
        return dbBackend.tx(backend -> {
            backend.enterConductDrug(drug);
            return drug.conductDrugId;
        });
    }

    @Path("get-conduct-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductDrugDTO getConductDrug(@QueryParam("conduct-drug-id") int conductDrugId) {
        return dbBackend.query(backend -> backend.getConductDrug(conductDrugId));
    }

    @Path("delete-conduct-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteConductDrug(@QueryParam("conduct-drug-id") int conductDrugId) {
        dbBackend.txProc(backend -> backend.deleteConductDrug(conductDrugId));
    }

    @Path("list-conduct-drug")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductDrugDTO> listConductDrug(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductDrug(conductId));
    }

    @Path("get-conduct-drug-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductDrugFullDTO getConductDrugFull(@QueryParam("conduct-drug-id") int conductDrugId) {
        return dbBackend.query(backend -> backend.getConductDrugFull(conductDrugId));
    }

    @Path("list-conduct-drug-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductDrugFullDTO> listConductDrugFull(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductDrugFull(conductId));
    }

    @Path("enter-conduct-kizai")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterConductKizai(ConductKizaiDTO kizai) {
        return dbBackend.tx(backend -> {
            backend.enterConductKizai(kizai);
            return kizai.conductKizaiId;
        });
    }

    @Path("get-conduct-kizai")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductKizaiDTO getConductKizai(@QueryParam("conduct-kizai-id") int conductKizaiId) {
        return dbBackend.query(backend -> backend.getConductKizai(conductKizaiId));
    }

    @Path("delete-conduct-kizai")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteConductKizai(@QueryParam("conduct-kizai-id") int conductKizaiId) {
        dbBackend.txProc(backend -> backend.deleteConductKizai(conductKizaiId));
    }

    @Path("list-conduct-kizai")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductKizaiDTO> listConductKizai(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductKizai(conductId));
    }

    @Path("get-conduct-kizai-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ConductKizaiFullDTO getConductKizaiFull(@QueryParam("conduct-kizai-id") int conductKizaiId) {
        return dbBackend.query(backend -> backend.getConductKizaiFull(conductKizaiId));
    }

    @Path("list-conduct-kizai-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ConductKizaiFullDTO> listConductKizaiFull(@QueryParam("conduct-id") int conductId) {
        return dbBackend.query(backend -> backend.listConductKizaiFull(conductId));
    }

    @Path("get-shahokokuho")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShahokokuhoDTO getShahokokuho(@QueryParam("shahokokuho-id") int shahokokuhoId) {
        return dbBackend.query(backend -> backend.getShahokokuho(shahokokuhoId));
    }

    @Path("enter-shahokokuho")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterShahokokuho(ShahokokuhoDTO shahokokuho) {
        return dbBackend.tx(backend -> {
            backend.enterShahokokuho(shahokokuho);
            return shahokokuho.shahokokuhoId;
        });
    }

    @Path("find-available-shahokokuho")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShahokokuhoDTO> findAvailableShahokokuho(@QueryParam("patient-id") int patientId, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findAvailableShahokokuho(patientId, at));
    }

    @Path("get-koukikourei")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public KoukikoureiDTO getKoukikourei(@QueryParam("koukikourei-id") int koukikoureiId) {
        return dbBackend.query(backend -> backend.getKoukikourei(koukikoureiId));
    }

    @Path("find-available-koukikourei")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<KoukikoureiDTO> findAvailableKoukikourei(@QueryParam("patient-id") int patientId, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findAvailableKoukikourei(patientId, at));
    }

    @Path("get-roujin")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public RoujinDTO getRoujin(@QueryParam("roujin-id") int roujinId) {
        return dbBackend.query(backend -> backend.getRoujin(roujinId));
    }

    @Path("find-available-roujin")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<RoujinDTO> findAvailableRoujin(@QueryParam("patient-id") int patientId, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findAvailableRoujin(patientId, at));
    }

    @Path("get-kouhi")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public KouhiDTO getKouhi(@QueryParam("kouhi-id") int kouhiId) {
        return dbBackend.query(backend -> backend.getKouhi(kouhiId));
    }

    @Path("find-available-kouhi")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<KouhiDTO> findAvailableKouhi(@QueryParam("patient-id") int patientId, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findAvailableKouhi(patientId, at));
    }

    @Path("enter-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterDisease(DiseaseDTO disease) {
        return dbBackend.tx(backend -> {
            backend.enterDisease(disease);
            return disease.diseaseId;
        });
    }

    @Path("enter-new-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterNewDisease(DiseaseNewDTO disease) {
        return dbBackend.tx(backend -> {
            backend.enterNewDisease(disease);
            return disease.disease.diseaseId;
        });
    }

    @Path("get-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DiseaseDTO getDisease(@QueryParam("disease-id") int diseaseId) {
        return dbBackend.query(backend -> backend.getDisease(diseaseId));
    }

    @Path("update-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updateDisease(DiseaseDTO disease) {
        dbBackend.txProc(backend -> backend.updateDisease(disease));
    }

    @Path("delete-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteDisease(@QueryParam("disease-id") int diseaseId) {
        dbBackend.txProc(backend -> backend.deleteDisease(diseaseId));
    }

    @Path("get-disease-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public DiseaseFullDTO getDiseaseFull(@QueryParam("disease-id") int diseaseId) {
        return dbBackend.query(backend -> backend.getDiseaseFull(diseaseId));
    }

    @Path("list-current-disease-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DiseaseFullDTO> listCurrentDiseaseFull(@QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.listCurrentDiseaseFull(patientId));
    }

    @Path("list-disease-full")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DiseaseFullDTO> listDiseaseFull(@QueryParam("patient-id") int patientId) {
        return dbBackend.query(backend -> backend.listDiseaseFull(patientId));
    }

    @Path("batch-update-disease-end-reason")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void batchUpdateDiseaseEndReason(List<DiseaseModifyEndReasonDTO> modifications) {
        dbBackend.txProc(backend -> backend.batchUpdateDiseaseEndReason(modifications));
    }

    @Path("enter-disease-adj")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterDiseaseAdj(DiseaseAdjDTO adj) {
        return dbBackend.tx(backend -> {
            backend.enterDiseaseAdj(adj);
            return adj.diseaseAdjId;
        });
    }

    @Path("delete-disease-adj")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deleteDiseaseAdj(@QueryParam("disease-adj-id") int diseaseAdjId) {
        dbBackend.txProc(backend -> backend.deleteDiseaseAdj(diseaseAdjId));
    }

    @Path("list-disease-adj")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<DiseaseAdjDTO> listDiseaseAdj(@QueryParam("disease-id") int diseaseId) {
        return dbBackend.query(backend -> backend.listDiseaseAdj(diseaseId));
    }

    @Path("delete-disease-adj-for-disease")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int deleteDiseaseAdjForDisease(DiseaseDTO disease) {
        return dbBackend.tx(backend -> backend.deleteDiseaseAdjForDisease(disease));
    }

    @Path("get-pharma-queue")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public PharmaQueueDTO getPharmaQueue(@QueryParam("visit-id") int visitId) {
        return dbBackend.query(backend -> backend.getPharmaQueue(visitId));
    }

    @Path("enter-pharma-queue")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void enterPharmaQueue(PharmaQueueDTO pharmaQueue) {
        dbBackend.txProc(backend -> backend.enterPharmaQueue(pharmaQueue));
    }

    @Path("delete-pharma-queue")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deletePharmaQueue(@QueryParam("visit-id") int visitId) {
        dbBackend.txProc(backend -> backend.deletePharmaQueue(visitId));
    }

    @Path("find-shinryou-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouMasterDTO findShinryouMasterByName(@QueryParam("name") String name, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findShinryouMasterByName(name, at));
    }

    @Path("resolve-shinryou-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public ShinryouMasterDTO resolveShinryouMasterByName(List<String> nameCandidates, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.resolveShinryouMasterByName(nameCandidates, at));
    }

    @Path("batch-resolve-shinryou-names")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Map<String, Integer> batchResolveShinryouNames(List<List<String>> args, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.batchResolveShinryouNames(args, at));
    }

    @Path("search-shinryou-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShinryouMasterDTO> searchShinryouMaster(@QueryParam("text") String text, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.searchShinryouMaster(text, at));
    }

    @Path("get-shinryou-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShinryouMasterDTO getShinryouMaster(@QueryParam("shinryoucode") int shinryoucode, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.getShinryouMaster(shinryoucode, at));
    }

    @Path("get-iyakuhin-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public IyakuhinMasterDTO getIyakuhinMaster(@QueryParam("iyakuhincode") int iyakuhincode, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.getIyakuhinMaster(iyakuhincode, at));
    }

    @Path("search-iyakuhin-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<IyakuhinMasterDTO> searchIyakuhinMaster(@QueryParam("text") String text, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.searchIyakuhinMaster(text, at));
    }

    @Path("get-kizai-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public KizaiMasterDTO getKizaiMaster(@QueryParam("kizaicode") int kizaicode, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.getKizaiMaster(kizaicode, at));
    }

    @Path("find-kizai-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public KizaiMasterDTO findKizaiMasterByName(@QueryParam("name") String name, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.findKizaiMasterByName(name, at));
    }

    @Path("resolve-kizai-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public KizaiMasterDTO resolveKizaiMasterByName(List<String> nameCandidates, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.resolveKizaiMasterByName(nameCandidates, at));
    }

    @Path("batch-resolve-kizai-names")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Map<String, Integer> batchResolveKizaiNames(List<List<String>> args, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.batchResolveKizaiNames(args, at));
    }

    @Path("search-kizai-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<KizaiMasterDTO> searchKizaiMaster(@QueryParam("text") String text, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.searchKizaiMaster(text, at));
    }

    @Path("search-byoumei-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ByoumeiMasterDTO> searchByoumeiMaster(@QueryParam("text") String text, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.searchByoumeiMaster(text, at));
    }

    @Path("get-byoumei-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ByoumeiMasterDTO getByoumeiMasterByName(@QueryParam("name") String name, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.getByoumeiMasterByName(name, at));
    }

    @Path("search-shuushokugo-master")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(@QueryParam("text") String text, @QueryParam("at") LocalDate at) {
        return dbBackend.query(backend -> backend.searchShuushokugoMaster(text, at));
    }

    @Path("get-shuushokugo-master-by-name")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ShuushokugoMasterDTO getShuushokugoMasterByName(@QueryParam("name") String name) {
        return dbBackend.query(backend -> backend.getShuushokugoMasterByName(name));
    }

    @Path("enter-presc-example")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public int enterPrescExample(PrescExampleDTO prescExample) {
        return dbBackend.tx(backend -> {
            backend.enterPrescExample(prescExample);
            return prescExample.prescExampleId;
        });
    }

    @Path("delete-presc-example")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public void deletePrescExample(@QueryParam("presc-example-id") int prescExampleId) {
        dbBackend.txProc(backend -> backend.deletePrescExample(prescExampleId));
    }

    @Path("update-presc-example")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public void updatePrescExample(PrescExampleDTO prescExample) {
        dbBackend.txProc(backend -> backend.updatePrescExample(prescExample));
    }

    @Path("search-presc-example")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PrescExampleFullDTO> searchPrescExample(@QueryParam("text") String text) {
        return dbBackend.query(backend -> backend.searchPrescExample(text));
    }

    @Path("list-all-presc-example")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PrescExampleFullDTO> listAllPrescExample() {
        return dbBackend.query(backend -> backend.listAllPrescExample());
    }

    @Path("get-last-practice-log")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public PracticeLogDTO getLastPracticeLog() {
        return dbBackend.query(backend -> backend.getLastPracticeLog());
    }

    @Path("get-last-practice-log-id")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public int getLastPracticeLogId() {
        return dbBackend.query(backend -> backend.getLastPracticeLogId());
    }

    @Path("list-practice-log-since")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PracticeLogDTO> listPracticeLogSince(@QueryParam("after-this-id") int afterThisId) {
        return dbBackend.query(backend -> backend.listPracticeLogSince(afterThisId));
    }
}
