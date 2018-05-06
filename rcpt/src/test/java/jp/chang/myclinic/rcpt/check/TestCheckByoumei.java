package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.rcpt.builder.ShinryouFullBuilder;
import jp.chang.myclinic.rcpt.builder.VisitFull2Builder;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCheckByoumei extends Base {

    @Test
    public void checkByoumeiPSA() throws Exception {
        Scope scope = createScope();
        scope.patient.patientId = 1234;
        scope.visits.add(new VisitFull2Builder()
                .setVisitId(1000)
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.ＰＳＡ)
                        .build()
                )
                .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("1"));
        new CheckByoumei(scope).check();
        RecordedRequest req = server.takeRequest();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/enter-disease", req.getPath());
    }

    @Test
    public void checkByoumeiHbA1c() throws Exception {
        Scope scope = createScope();
        scope.patient.patientId = 1234;
        scope.visits.add(new VisitFull2Builder()
                .setVisitId(1000)
                .addShinryou(new ShinryouFullBuilder()
                        .setShinryoucode(shinryouMap.ＨｂＡ１ｃ)
                        .build()
                )
                .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("1"));
        new CheckByoumei(scope).check();
        RecordedRequest req = server.takeRequest();
        assertEquals(1, nerror);
        assertEquals("POST", req.getMethod());
        assertEquals("/enter-disease", req.getPath());
    }

}
