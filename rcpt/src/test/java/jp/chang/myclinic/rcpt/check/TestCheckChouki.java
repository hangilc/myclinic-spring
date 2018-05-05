package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
    public void passOK() {
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
        assertEquals("1 chouki", 0, state.nerror);
    }

    @Test
    public void shouldDetectMissing() {
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

    @Test
    public void shohouryouChoukiDuplicate() {
        Scope scope = createScope();
        scope.visits.add(createVisit(visit -> {
            visit.drugs.add(createDrug());
            visit.shinryouList.add(createShinryou(shinryouMap.調基));
            visit.shinryouList.add(createShinryou(shinryouMap.処方せん料));
        }));
        class State {
            private int nerror = 0;
            private String message = "";
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
            state.message = err.getMessage();
        };
        new CheckChouki(scope).check();
        assertEquals("shohouryu chouki dupliate", 1, state.nerror);
        assertEquals("shohouryu chouki dupliate (message)", "処方せん料、調基の同時算定", state.message);
    }

    @Test
    public void choukiWithNoDrug() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("1"));
        Scope scope = createScope();
        scope.fixit = true;
        scope.visits.add(createVisit(visit -> {
            visit.shinryouList.add(createShinryou(shinryouMap.調基, s -> {
                s.shinryou.shinryouId = 1234;
            }));
        }));
        class State {
            private int nerror = 0;
        }
        State state = new State();
        scope.errorHandler = err -> {
            state.nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, state.nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/json/batch-delete-shinryou", httpUrl.encodedPath());
        assertEquals("shinryou-id=1234", httpUrl.url().getQuery());
    }

    private Scope createScope() {
        Scope scope = new Scope();
        scope.visits = new ArrayList<>();
        scope.patient = createPatient();
        scope.resolvedMasterMap = masterMaps.resolvedMap;
        scope.shinryouByoumeiMap = masterMaps.shinryouByoumeiMap;
        scope.api = Service.api;
        return scope;
    }

    private PatientDTO createPatient() {
        PatientDTO patient = new PatientDTO();
        patient.patientId = 100;
        patient.lastName = "LastName";
        patient.firstName = "FirstName";
        return patient;
    }

    private VisitFull2DTO createVisit(Consumer<VisitFull2DTO> cb) {
        VisitFull2DTO visit = new VisitFull2DTO();
        visit.shinryouList = new ArrayList<>();
        visit.drugs = new ArrayList<>();
        cb.accept(visit);
        return visit;
    }

    private DrugFullDTO createDrug(Consumer<DrugFullDTO> cb) {
        DrugFullDTO drug = new DrugFullDTO();
        cb.accept(drug);
        return drug;
    }

    private DrugFullDTO createDrug() {
        return createDrug(d -> {
        });
    }

    private ShinryouFullDTO createShinryou(int shinryoucode, Consumer<ShinryouFullDTO> cb) {
        ShinryouFullDTO shinryou = new ShinryouFullDTO();
        shinryou.shinryou = new ShinryouDTO();
        shinryou.shinryou.shinryoucode = shinryoucode;
        shinryou.master = new ShinryouMasterDTO();
        shinryou.master.shinryoucode = shinryoucode;
        cb.accept(shinryou);
        return shinryou;
    }

    private ShinryouFullDTO createShinryou(int shinryoucode) {
        return createShinryou(shinryoucode, s -> {
        });
    }
}
