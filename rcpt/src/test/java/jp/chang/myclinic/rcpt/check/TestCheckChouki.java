package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.rcpt.builder.C;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestCheckChouki extends Base {

    @After
    public void doAfter() {
        nerror = 0;
    }

    @Test
    public void passOK() {
        Scope scope = createScope();
        scope.visits.add(C.createVisitFull(visit -> {
            visit.drugs = List.of(C.createDrugFull(null));
            visit.shinryouList = List.of(mkShinryouFull(shinryouMap.調基));
        }));
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("1 chouki", 0, nerror);
    }

    @Test
    public void shohouryouChoukiDuplicate() {
        Scope scope = createScope();
        scope.visits.add(C.createVisitFull(visit -> {
            visit.drugs = List.of(C.createDrugFull(null));
            visit.shinryouList = List.of(
                    mkShinryouFull(shinryouMap.調基),
                    mkShinryouFull(shinryouMap.処方せん料)
            );
        }));
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("shohouryu chouki dupliate", 1, nerror);
    }

    @Test
    public void choukiWithNoDrug() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("true"));
        Scope scope = createScope();
        scope.visits.add(C.createVisitFull(visit -> {
            visit.drugs = Collections.emptyList();
            visit.shinryouList = List.of(
                mkShinryouFull(shinryouMap.再診, s -> s.shinryou.shinryouId = 1234),
                mkShinryouFull(shinryouMap.調基, s -> s.shinryou.shinryouId = 1235)
            );
        }));
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/json/batch-delete-shinryou", httpUrl.encodedPath());
        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
        assertEquals("1235", httpUrl.queryParameter("shinryou-id"));
    }

    @Test
    public void tooManyChouki() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("true"));
        Scope scope = createScope();
        scope.visits.add(C.createVisitFull(visit -> {
            visit.drugs = List.of(C.createDrugFull(null));
            visit.shinryouList = List.of(
                    mkShinryouFull(shinryouMap.再診, s -> s.shinryou.shinryouId = 1235),
                    mkShinryouFull(shinryouMap.調基, s -> s.shinryou.shinryouId = 1236),
                    mkShinryouFull(shinryouMap.調基, s -> s.shinryou.shinryouId = 1237)
            );
        }));
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/json/batch-delete-shinryou", httpUrl.encodedPath());
        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
        assertEquals("1237", httpUrl.queryParameter("shinryou-id"));
    }

    @Test
    public void choukiMissing() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("1"));
        Scope scope = createScope();
        scope.visits.add(C.createVisitFull(visit -> {
            visit.visit = C.createVisit(v -> v.visitId = 1000);
            visit.drugs = List.of(C.createDrugFull(null));
            visit.shinryouList = List.of(
                    mkShinryouFull(shinryouMap.再診, s -> s.shinryou.shinryouId = 1235)
            );
        }));
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        ObjectMapper mapper = new ObjectMapper();
        ShinryouDTO shinryou = mapper.readValue(req.getBody().readUtf8(), ShinryouDTO.class);
        ShinryouDTO expected = C.createShinryou(s -> {
            s.visitId = 1000;
            s.shinryoucode = shinryouMap.調基;
        });
        System.out.println(shinryou);
        assertEquals("/json/enter-shinryou", httpUrl.encodedPath());
        assertEquals(expected, shinryou);
    }

}
