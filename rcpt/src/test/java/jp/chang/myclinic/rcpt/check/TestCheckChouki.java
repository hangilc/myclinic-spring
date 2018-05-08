package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.rcpt.builder.Clinic;
import jp.chang.myclinic.rcpt.builder.ShinryouBuilder;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckChouki extends Base {

    private int nerror;

    @After
    public void doAfter(){
        nerror = 0;
    }

    @Test
    public void passOK() {
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("1 chouki", 0, nerror);
    }

    @Test
    public void shohouryouChoukiDuplicate() {
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.調基);
        clinic.addShinryou(shinryouMap.処方せん料);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("shohouryu chouki dupliate", 1, nerror);
    }

    @Test
    public void choukiWithNoDrug() throws Exception {
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("true"));
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId, req);
    }

    @Test
    public void tooManyChouki() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("true"));
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        clinic.addShinryou(shinryouMap.再診);
        clinic.addShinryou(shinryouMap.調基);
        int shinryouId = clinic.addShinryou(shinryouMap.調基);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId, req);
        assertEquals("POST", req.getMethod());
    }

    @Test
    public void choukiMissing() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("1"));
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        int visitId = clinic.startVisit();
        clinic.addDrug();
        clinic.addShinryou(shinryouMap.再診);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        ObjectMapper mapper = new ObjectMapper();
        ShinryouDTO shinryou = mapper.readValue(req.getBody().readUtf8(), ShinryouDTO.class);
        ShinryouDTO expected = new ShinryouBuilder(s -> {
            s.shinryouId = 0;
            s.visitId = visitId;
            s.shinryoucode = shinryouMap.調基;
        }).build();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/enter-shinryou", httpUrl.encodedPath());
        assertEquals(expected, shinryou);
    }

}
