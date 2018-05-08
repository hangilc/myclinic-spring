package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.Sex;
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

    public PatientDTO createPatient(Consumer<PatientModifier> cb){
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
        return result;
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
        PatientDTO patient = createPatient(null);
        VisitFull2DTO visitFull = createVisitFull2DTO(patient.patientId, defaultVisitedAtDate);
        return visitFull.visit.visitId;
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

    public int addShinry(int shinryoucode, Consumer<ShinryouMasterModifier> cb){
        ShinryouMasterDTO master = shinryouMasterMap.get(shinryoucode);
        if( master == null ){
            master = createShinryouMaster(shinryoucode, cb);
        }
        ShinryouDTO shinryou = createShinryou(shinryoucode);
        ShinryouFullDTO shinryouFull = new ShinryouFullDTO();
        shinryouFull.shinryou = shinryou;
        shinryouFull.master = master;
        currentVisit.shinryouList.add(shinryouFull);
        return shinryou.shinryoucode;
    }

    private ShinryouDTO createShinryou(int shinryoucode){
        ShinryouDTO result = new ShinryouDTO();
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

}
