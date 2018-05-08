package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.rcpt.builder.DrugFullBuilder;
import jp.chang.myclinic.rcpt.builder.ShinryouBuilder;
import jp.chang.myclinic.rcpt.builder.VisitFull2Builder;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Test;

import java.util.Set;

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
        scope.visits.add(new VisitFull2Builder()
                .addDrug(new DrugFullBuilder().build())
                .addShinryou(
                        new ShinryouFullBuilder()
                                .setShinryoucode(shinryouMap.調基)
                                .build())
                .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
        };
        new CheckChouki(scope).check();
        assertEquals("1 chouki", 0, nerror);
    }

    @Test
    public void shohouryouChoukiDuplicate() {
        Scope scope = createScope();
        scope.visits.add(new VisitFull2Builder()
                .addDrug(new DrugFullBuilder().build())
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.調基)
                        .build()
                )
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.処方せん料)
                        .build()
                )
                .build()
        );
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
        scope.visits.add(new VisitFull2Builder()
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.再診)
                        .setShinryouId(1234)
                        .build()
                )
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.調基)
                        .setShinryouId(1235)
                        .build()
                )
                .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/batch-delete-shinryou", httpUrl.encodedPath());
        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
        assertEquals("1235", httpUrl.queryParameter("shinryou-id"));
    }

    @Test
    public void tooManyChouki() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("true"));
        Scope scope = createScope();
        scope.visits.add(new VisitFull2Builder()
                .addDrug(new DrugFullBuilder().build())
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.再診)
                        .setShinryouId(1235)
                        .build()
                )
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.調基)
                        .setShinryouId(1236)
                        .build()
                )
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.調基)
                        .setShinryouId(1237)
                        .build()
                )
                .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        new CheckChouki(scope).check();
        RecordedRequest req = server.takeRequest();
        HttpUrl httpUrl = req.getRequestUrl();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/batch-delete-shinryou", httpUrl.encodedPath());
        assertEquals(Set.of("shinryou-id"), httpUrl.queryParameterNames());
        assertEquals("1237", httpUrl.queryParameter("shinryou-id"));
    }

    @Test
    public void choukiMissing() throws Exception {
        MockWebServer server = TestListener.server;
        server.enqueue(new MockResponse().setBody("1"));
        Scope scope = createScope();
        scope.visits.add(new VisitFull2Builder()
                .setVisitId(1000)
                .addDrug(new DrugFullBuilder().build())
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.再診)
                        .build())
                .build()
        );
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
            s.visitId = 1000;
            s.shinryoucode = shinryouMap.調基;
        }).build();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/enter-shinryou", httpUrl.encodedPath());
        assertEquals(expected, shinryou);
    }

}
