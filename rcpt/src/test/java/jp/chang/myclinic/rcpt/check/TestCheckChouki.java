package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class TestCheckChouki {

    private static LocalDate at = LocalDate.of(2018, 3, 1);
    private static Common.MasterMaps masterMaps = Common.getMasterMaps(at);
    private static ResolvedShinryouMap shinryouMap = masterMaps.resolvedMap.shinryouMap;

    @Test
    public void passOK(){
        Scope scope = createScope();
        scope.visits.add(createVisit(visit -> {
            visit.drugs.add(createDrug());
            visit.shinryouList.add(createShinryou(shinryouMap.調基));
        }));
        class State {
            private int nerror = 0;
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("no chouki", 0, state.nerror);
    }

    @Test
    public void shouldDetectMissing(){
        Scope scope = createScope();
        scope.visits.add(createVisit(visit -> {
            visit.drugs.add(createDrug());
        }));
        class State {
            private int nerror = 0;
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("no chouki", 1, state.nerror);
    }

    private Scope createScope(){
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = createPatient();
        scope.resolvedMasterMap = masterMaps.resolvedMap;
        scope.shinryouByoumeiMap = masterMaps.shinryouByoumeiMap;
        return scope;
    }

    private PatientDTO createPatient(){
        PatientDTO patient = new PatientDTO();
        patient.patientId = 100;
        patient.lastName = "LastName";
        patient.firstName = "FirstName";
        return patient;
    }

    private VisitFull2DTO createVisit(Consumer<VisitFull2DTO> cb){
        VisitFull2DTO visit = new VisitFull2DTO();
        visit.shinryouList = new ArrayList<>();
        visit.drugs = new ArrayList<>();
        cb.accept(visit);
        return visit;
    }

    private DrugFullDTO createDrug(Consumer<DrugFullDTO> cb){
        DrugFullDTO drug = new DrugFullDTO();
        cb.accept(drug);
        return drug;
    }

    private DrugFullDTO createDrug(){
        return createDrug(d -> {});
    }

    private ShinryouFullDTO createShinryou(int shinryoucode){
        ShinryouFullDTO shinryou = new ShinryouFullDTO();
        shinryou.shinryou = new ShinryouDTO();
        shinryou.shinryou.shinryoucode = shinryoucode;
        shinryou.master = new ShinryouMasterDTO();
        shinryou.master.shinryoucode = shinryoucode;
        return shinryou;
    }



}
