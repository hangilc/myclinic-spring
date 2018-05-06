package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Clinic {

    //private static Logger logger = LoggerFactory.getLogger(Clinic.class);
    private LocalDate defaultMasterValidFromDate = LocalDate.of(2018, 4, 1);
    private String defaultMasterValidFrom = defaultMasterValidFromDate.toString();
    private List<PatientDTO> patients = new ArrayList<>();
    private List<VisitFull2DTO> visits = new ArrayList<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterMap = new HashMap<>();
    private VisitDTO currentVisit;
    private List<ShinryouDTO> currentShinryouList = new ArrayList<>();
    private List<DrugDTO> currentDrugs = new ArrayList<>();

    public Clinic() {

    }

    public PatientDTO newPatient(){
        return new PatientBuilder().build();
    }

    public PatientDTO findPatient(int patientId){
        return patients.stream().filter(p -> p.patientId == patientId).findFirst().orElse(null);
    }

    public PatientDTO ensurePatient(int patientId){
        PatientDTO patient = findPatient(patientId);
        if( patient != null ){
            patient = new PatientBuilder()
                    .modify(p -> p.patientId = patientId)
                    .build();
            patients.add(patient);
        }
        return patient;
    }

    public VisitDTO startVisit(int patientId){
        ensurePatient(patientId);
        VisitDTO visit = new VisitBuilder()
                .modify(v -> {
                    v.patientId = patientId;
                    v.visitedAt = defaultMasterValidFrom;
                })
                .build();
        this.currentVisit = visit;
        this.currentShinryouList = new ArrayList<>();
        this.currentDrugs = new ArrayList<>();
        return visit;
    }

    public ShinryouMasterDTO ensureShinryouMaster(int shinryoucode){
        ShinryouMasterDTO m = shinryouMasterMap.get(shinryoucode);
        if( m == null ){
            m = new ShinryouMasterBuilder()
                    .modify(sm -> {
                        sm.shinryoucode = shinryoucode;
                        sm.validFrom = defaultMasterValidFrom;
                    })
                    .build();
            shinryouMasterMap.put(shinryoucode, m);
        }
        return m;
    }

    public ShinryouMasterDTO findShinryouMaster(int shinryoucode){
        return shinryouMasterMap.get(shinryoucode);
    }

    public ShinryouDTO addShinryou(int shinryoucode){
        ensureShinryouMaster(shinryoucode);
        ShinryouDTO shinryou = new ShinryouBuilder()
                .modify(s -> {
                    s.shinryoucode = shinryoucode;
                    s.visitId = currentVisit.visitId;
                })
                .build();
        currentShinryouList.add(shinryou);
        return shinryou;
    }

    public void endVisit(){
        VisitFull2DTO visit = new VisitFull2DTO();
        visit.visit = currentVisit;
        visit.shinryouList = currentShinryouList.stream()
                .map(s -> {
                    ShinryouFullDTO result = new ShinryouFullDTO();
                    result.shinryou = s;
                    result.master = ensureShinryouMaster(s.shinryoucode);
                    return result;
                })
                .collect(Collectors.toList());
        visit.drugs = currentDrugs.stream()
                .map(d -> {
                    DrugFullDTO result = new DrugFullDTO();
                    return result;
                })
                .collect(Collectors.toList());
        visit.conducts = new ArrayList<>();
        visit.texts = new ArrayList<>();
        visit.charge = new ChargeDTO();
        visit.hoken = new HokenDTO();
        visits.add(visit);
        currentVisit = null;
        currentShinryouList = new ArrayList<>();
        currentDrugs = new ArrayList<>();
    }

    public List<VisitFull2DTO> getVisits(){
        if( currentVisit != null ){
            endVisit();
        }
        return visits;
    }

}
