package jp.chang.myclinic.rcpt.check;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

public class TestCheckByoumei extends Base {

    @Test
    public void checkByoumei() {
        Scope scope = createScope();
        scope.patient.patientId = 1234;
        scope.visits.add(
                B.visitFullBuilder()
                        .buildVisit(b -> b.setVisitId(1000))
                        .addShinryou(b -> b.setShinryoucode(shinryouMap.ＰＳＡ))
                        .build()
        );
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("1"));
        new CheckByoumei(scope).check();
        assertEquals(1, nerror);
    }

}
