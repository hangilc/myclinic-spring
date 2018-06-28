package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.*;
import jp.chang.myclinic.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Clinic {

    private LocalDate defaultMasterValidFromDate = LocalDate.of(2018, 4, 1);
    private LocalDateTime defaultVisitedAtDateTime =
            LocalDateTime.of(defaultMasterValidFromDate, LocalTime.of(10, 20));
    private int nextPatientId = 1;
    private int nextShinryouId = 1;
    private int nextDrugId = 1;
    private int nextDiseaseId = 1;
    private int nextVisitId = 1;
    private Map<Integer, PatientDTO> patientMap = new HashMap<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterMap = new HashMap<>();
    private Map<Integer, IyakuhinMasterDTO> iyakuhinMasterMap = new HashMap<>();
    private Map<Integer, ByoumeiMasterDTO> byoumeiMasterMap = new HashMap<>();
    private Map<Integer, ShuushokugoMasterDTO> shuushokugoMasterMap = new HashMap<>();
    private List<DiseaseFullDTO> diseases = new ArrayList<>();
    private List<VisitFull2DTO> visits = new ArrayList<>();
    private VisitFull2DTO currentVisit;
    private static DateTimeFormatter sqlDateTimeFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    public Clinic() {

    }

    public LocalDate getBaseDate(){
        return defaultMasterValidFromDate;
    }

    private PatientDTO findPatient(int patientId){
        return patientMap.get(patientId);
    }

    public int createPatient(){
        return createPatient(null);
    }

    public int createPatient(Consumer<PatientModifier> cb){
        PatientDTO result = new PatientDTO();
        result.patientId = nextPatientId++;
        result.lastName = G.gensym();
        result.firstName = G.gensym();
        result.lastNameYomi = G.gensym();
        result.firstNameYomi = G.gensym();
        result.birthday = LocalDate.of(1970, 2, 12).toString();
        result.sex = Sex.Female.getCode();
        result.address = G.gensym();
        result.phone = G.gensym();
        if( cb != null ){
            cb.accept(new PatientModifier(result));
        }
        patientMap.put(result.patientId, result);
        return result.patientId;
    }

    private VisitDTO createVisit(int patientId,Consumer<VisitModifier> cb){
        VisitDTO visit = new VisitDTO();
        visit.visitId = nextVisitId++;
        visit.patientId = patientId;
        visit.visitedAt = defaultVisitedAtDateTime.format(sqlDateTimeFormatter);
        if( cb != null ){
            cb.accept(new VisitModifier(visit));
        }
        return visit;
    }

    private VisitFull2DTO createVisitFull2DTO(int patientId, Consumer<VisitModifier> cb){
        VisitFull2DTO result = new VisitFull2DTO();
        result.visit = createVisit(patientId, cb);
        result.texts = new ArrayList<>();
        result.shinryouList = new ArrayList<>();
        result.drugs = new ArrayList<>();
        result.conducts = new ArrayList<>();
        result.hoken = new HokenDTO();
        return result;
    }

    public int startVisit() {
        return startVisit(null);
    }

    public int startVisit(Consumer<VisitModifier> cb){
        int patientId = createPatient(null);
        return startVisit(patientId, cb);
    }

    public int startVisit(int patientId){
        return startVisit(patientId, null);
    }

    public int startVisit(int patientId, Consumer<VisitModifier> cb){
        VisitFull2DTO visitFull = createVisitFull2DTO(patientId, cb);
        currentVisit = visitFull;
        return visitFull.visit.visitId;
    }

    public int getVisitId(){
        return currentVisit.visit.visitId;
    }

    public String getVisitedAt(){
        return currentVisit.visit.visitedAt;
    }

    public int createShinryouMaster(){
        int shinryoucode = 0;
        for(int i=1;i<100;i++){
            ShinryouMasterDTO m = shinryouMasterMap.get(i);
            if( m == null ){
                shinryoucode = i;
                break;
            }
        }
        if( shinryoucode == 0 ){
            throw new RuntimeException("Cannot allocate new shinryoucode.");
        }
        return createShinryouMaster(shinryoucode, null);
    }

    public int createShinryouMaster(int shinryoucode){
        return createShinryouMaster(shinryoucode, null);
    }

    public int createShinryouMaster(int shinryoucode,
                                                   Consumer<ShinryouMasterModifier> cb){
        ShinryouMasterDTO result = new ShinryouMasterDTO();
        result.shinryoucode = shinryoucode;
        result.validFrom = defaultMasterValidFromDate.toString();
        result.validUpto = "0000-00-00";
        result.name = G.gensym();
        result.codeAlpha = 'D';
        result.codeKubun = "007";
        result.codeBu = "03";
        result.codeShou = '2';
        result.roujinTekiyou = '0';
        result.oushinkubun = '0';
        result.houkatsukensa = "00";
        result.kensaGroup = "03";
        result.tensuu = 144;
        result.tensuuShikibetsu = '3';
        result.shuukeisaki = "600";
        if( cb != null ){
            cb.accept(new ShinryouMasterModifier(result));
        }
        shinryouMasterMap.put(shinryoucode, result);
        return result.shinryoucode;
    }

    public int addShinryou(int shinryoucode){
        return addShinryou(shinryoucode, null);
    }

    public int addShinryou(int shinryoucode, Consumer<ShinryouMasterModifier> cb){
        if( currentVisit == null ){
            startVisit();
        }
        ShinryouMasterDTO master = shinryouMasterMap.get(shinryoucode);
        if( master == null ){
            createShinryouMaster(shinryoucode, cb);
            master = shinryouMasterMap.get(shinryoucode);
        }
        ShinryouDTO shinryou = createShinryou(shinryoucode);
        ShinryouFullDTO shinryouFull = new ShinryouFullDTO();
        shinryouFull.shinryou = shinryou;
        shinryouFull.master = master;
        currentVisit.shinryouList.add(shinryouFull);
        return shinryou.shinryouId;
    }

    private ShinryouDTO createShinryou(int shinryoucode){
        ShinryouDTO result = new ShinryouDTO();
        result.shinryouId = nextShinryouId++;
        result.shinryoucode = shinryoucode;
        result.visitId = currentVisit.visit.visitId;
        return result;
    }

    public void endVisit(){
        visits.add(currentVisit);
        currentVisit = null;
    }

    public List<VisitFull2DTO> getVisits(){
        if( currentVisit != null ){
            endVisit();
        }
        return visits;
    }

    public int createIyakuhinMaster(Consumer<IyakuhinMasterModifier> cb){
        int iyakuhincode = 0;
        for(int i=0;i<100;i++){
            iyakuhincode = G.genid();
            if( iyakuhinMasterMap.get(iyakuhincode) == null ){
                break;
            }
            iyakuhincode = 0;
        }
        if( iyakuhincode == 0 ){
            throw new RuntimeException("Cannot find iyakuhincode for new master.");
        }
        IyakuhinMasterDTO result = new IyakuhinMasterDTO();
        result.iyakuhincode = iyakuhincode;
        result.name = G.gensym();
        result.yomi = G.gensym();
        result.unit = G.gensym();
        result.yakka = 39.1;
        result.madoku = '0';
        result.kouhatsu = 1;
        result.zaikei = 1;
        result.validFrom = defaultMasterValidFromDate.toString();
        result.validUpto = "0000-00-00";
        if( cb != null ) {
            cb.accept(new IyakuhinMasterModifier(result));
        }
        iyakuhinMasterMap.put(iyakuhincode, result);
        return result.iyakuhincode;
    }

    private DrugDTO createDrug(IyakuhinMasterDTO master){
        DrugDTO result = new DrugDTO();
        result.drugId = nextDrugId++;
        result.visitId = G.genid();
        result.iyakuhincode = master.iyakuhincode;
        result.amount = 3.0;
        if( Zaikei.fromCode(master.zaikei) == Zaikei.Gaiyou ){
            result.category = DrugCategory.Gaiyou.getCode();
            result.days = 1;
        } else {
            result.category = DrugCategory.Naifuku.getCode();
            result.days = 7;
        }
        result.prescribed = 0;
        return result;
    }

    private DrugFullDTO createDrugFull(DrugDTO drug){
        DrugFullDTO result = new DrugFullDTO();
        result.drug = drug;
        result.master = iyakuhinMasterMap.get(drug.iyakuhincode);
        return result;
    }

    public int addDrug(){
        return addDrug(null);
    }

    public int addDrug(Consumer<IyakuhinMasterModifier> masterModifier){
        return addDrug(masterModifier, null);
    }

    public int addDrug(Consumer<IyakuhinMasterModifier> masterModifier,
                       Consumer<DrugModifier> drugModifier){
        if( currentVisit == null ){
            startVisit();
        }
        int iyakuhincode = createIyakuhinMaster(masterModifier);
        IyakuhinMasterDTO master = iyakuhinMasterMap.get(iyakuhincode);
        DrugDTO drug = createDrug(master);
        if( drugModifier != null ){
            drugModifier.accept(new DrugModifier(drug));
        }
        DrugFullDTO drugFull = createDrugFull(drug);
        currentVisit.drugs.add(drugFull);
        return drug.drugId;
    }

    public int addGaiyouDrug(){
        return addDrug(modifier -> modifier.setZaikei(Zaikei.Gaiyou),
                modifier -> modifier.setCategory(DrugCategory.Gaiyou));
    }

    public int addMadokuDrug(){
        return addDrug(modifier -> modifier.setMadoku(Madoku.Kouseishinyaku),
                modifier -> modifier.setCategory(DrugCategory.Naifuku));
    }

    public int addNaifukuDrug(){
        return addNaifukuDrug(null);
    }

    public int addNaifukuDrug(Consumer<DrugModifier> cb){
        return addDrug(m -> m.setZaikei(Zaikei.Naifuku),
                m -> {
                    m.setCategory(DrugCategory.Naifuku);
                    if( cb != null ) {
                        cb.accept(m);
                    }
                });
    }

    public List<Integer> addChoukiNaifukuDrug(int count){
        List<Integer> drugIds = new ArrayList<>();
        for(int i=0;i<count;i++){
            int drugId = addNaifukuDrug(m -> m.setDays(28));
            drugIds.add(drugId);
        }
        return drugIds;
    }

    private ByoumeiMasterDTO createByoumeiMaster(){
        int byoumeicode = 0;
        for(int i=1;i<100;i++){
            ByoumeiMasterDTO m = byoumeiMasterMap.get(i);
            if( m == null ){
                byoumeicode = i;
                break;
            }
        }
        if( byoumeicode == 0 ){
            throw new RuntimeException("Cannot allocate new byoumeicode.");
        }
        ByoumeiMasterDTO master = new ByoumeiMasterDTO();
        master.shoubyoumeicode = byoumeicode;
        master.name = G.gensym();
        master.validFrom = defaultMasterValidFromDate.toString();
        master.validUpto = "0000-00-00";
        return master;
    }

    public int addDisease(){
        return addDisease(null);
    }

    public int addDisease(Consumer<DiseaseModifier> cb){
        if( currentVisit == null ){
            startVisit();
        }
        ByoumeiMasterDTO byoumeiMaster = createByoumeiMaster();
        DiseaseDTO disease = new DiseaseDTO();
        disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
        disease.diseaseId = nextDiseaseId++;
        disease.startDate = currentVisit.visit.visitedAt.substring(0, 10);
        disease.endDate = "0000-00-00";
        disease.endReason = DiseaseEndReason.NotEnded.getCode();
        disease.patientId = currentVisit.visit.patientId;
        if( cb != null ){
            cb.accept(new DiseaseModifier(disease));
        }
        DiseaseFullDTO diseaseFull = new DiseaseFullDTO();
        diseaseFull.disease = disease;
        diseaseFull.master = byoumeiMaster;
        diseaseFull.adjList = new ArrayList<>();
        diseases.add(diseaseFull);
        return disease.diseaseId;
    }

    public List<DiseaseFullDTO> getDiseases(){
        return diseases;
    }

}
