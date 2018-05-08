package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.consts.Zaikei;
import jp.chang.myclinic.dto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Clinic {

    private LocalDate defaultMasterValidFromDate = LocalDate.of(2018, 4, 1);
    private LocalDate defaultVisitedAtDate = defaultMasterValidFromDate;
    private int nextPatientId = 1;
    private int nextShinryouId = 1;
    private int nextDrugId = 1;
    private Map<Integer, PatientDTO> patientMap = new HashMap<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterMap = new HashMap<>();
    private Map<Integer, IyakuhinMasterDTO> iyakuhinMasterMap = new HashMap<>();
    private List<VisitFull2DTO> visits = new ArrayList<>();
    private VisitFull2DTO currentVisit;

    public Clinic() {

    }

    public PatientDTO newPatient(){
        return new PatientBuilder().build();
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

    private VisitDTO createVisit(int patientId, LocalDate at){
        VisitDTO visit = new VisitDTO();
        visit.patientId = patientId;
        visit.visitedAt = at.toString();
        return visit;
    }

    private VisitFull2DTO createVisitFull2DTO(int patientId, LocalDate at){
        VisitFull2DTO result = new VisitFull2DTO();
        result.visit = createVisit(patientId, at);
        result.texts = new ArrayList<>();
        result.shinryouList = new ArrayList<>();
        result.drugs = new ArrayList<>();
        result.conducts = new ArrayList<>();
        result.hoken = new HokenDTO();
        return result;
    }

    public int startVisit(){
        int patientId = createPatient(null);
        return startVisit(patientId);
    }

    public int startVisit(int patientId){
        VisitFull2DTO visitFull = createVisitFull2DTO(patientId, defaultVisitedAtDate);
        currentVisit = visitFull;
        return visitFull.visit.visitId;
    }

    public String getVisitedAt(){
        return currentVisit.visit.visitedAt;
    }

    private ShinryouMasterDTO createShinryouMaster(int shinryoucode,
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
        return result;
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
            master = createShinryouMaster(shinryoucode, cb);
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
        result.shuukeisaki = 0; // not used
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
        if( currentVisit == null ){
            startVisit();
        }
        int iyakuhincode = createIyakuhinMaster(masterModifier);
        IyakuhinMasterDTO master = iyakuhinMasterMap.get(iyakuhincode);
        DrugDTO drug = createDrug(master);
        DrugFullDTO drugFull = createDrugFull(drug);
        currentVisit.drugs.add(drugFull);
        return drug.drugId;
    }

}
