package jp.chang.myclinic.clientmock.dbgateway;

import jp.chang.myclinic.clientmock.entity.*;
import jp.chang.myclinic.dto.*;

import java.time.LocalDate;
import java.util.List;

public class DbGatewayMock {

    private PatientRepoInterface patientRepo = new PatientRepo();
    private ShahokokuhoRepoInterface shahokokuhoRepo = new ShahokokuhoRepo();
    private KoukikoureiRepoInterface koukikoureiRepo = new KoukikoureiRepo();
    private RoujinRepoInterface roujinRepo = new RoujinRepo();
    private KouhiRepoInterface kouhiRepo = new KouhiRepo();
    private VisitRepoInterface visitRepo = new VisitRepo();
    private WqueueRepoInterface wqueueRepo = new WqueueRepo();
    private ShinryouRepoInterface shinryouRepo = new ShinryouRepo();
    private DrugRepoInterface drugRepo = new DrugRepo();

    public int enterPatient(PatientDTO patient){
        return patientRepo.enterPatient(patient);
    }

    public PatientDTO getPatient(int patientId){
        return patientRepo.getPatient(patientId);
    }

    public List<ShahokokuhoDTO> findAvailableShahokokuho(int patientId, LocalDate atDate) {
        return shahokokuhoRepo.findAvailableShahokokuho(patientId, atDate);
    }

    public List<KoukikoureiDTO> findAvailableKoukikourei(int patientId, LocalDate atDate) {
        return koukikoureiRepo.findAvailableKoukikourei(patientId, atDate);
    }

    public List<RoujinDTO> findAvailableRoujin(int patientId, LocalDate atDate) {
        return roujinRepo.findAvailableRoujin(patientId, atDate);
    }

    public List<KouhiDTO> findAvailableKouhi(int patientId, LocalDate atDate) {
        return kouhiRepo.findAvailableKouhi(patientId, atDate);
    }

    public int enterVisit(VisitDTO visit){
        return visitRepo.enterVisit(visit);
    }

    public void enterWqueue(WqueueDTO wqueue){
        wqueueRepo.enterWqueue(wqueue);
    }

    public List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds) {
        return shinryouRepo.batchGetShinryouAttr(shinryouIds);
    }

    public List<DrugAttrDTO> batchGetDrugAttr(List<Integer> drugIds) {
        return drugRepo.batchGetDrugAttr(drugIds);
    }

    public List<ShoukiDTO> batchGetShouki(List<Integer> visitIds) {
        return visitRepo.batchGetShouki(visitIds);
    }
}
