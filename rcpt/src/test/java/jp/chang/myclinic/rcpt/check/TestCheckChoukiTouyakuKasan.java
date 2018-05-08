package jp.chang.myclinic.rcpt.check;

public class TestCheckChoukiTouyakuKasan extends Base {

    //private static Logger logger = LoggerFactory.getLogger(TestCheckChoukiTouyakuKasan.class);
    private int nerror;
/*
    @After
    public void doAfter(){
        nerror = 0;
    }

    @Test
    public void checkConflict() throws Exception {
        Scope scope = createScope();
        Clinic clinic = new Clinic();
        clinic.startVisit();
        int shinryouId = clinic.addShinryou(shinryouMap.特定疾患処方);
        clinic.addShinryou(shinryouMap.長期処方);
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("true"));
        new CheckChoukiTouyakuKasan(scope).check();
        RecordedRequest req = server.takeRequest();
        assertEquals(1, nerror);
        assertBatchDeleteShinryou(shinryouId, req);
    }

    @Test
    public void tooManyChoukishohou() throws Exception {
        Clinic clinic = new Clinic();
        clinic.startVisit();
        clinic.addShinryou(shinryouMap.長期処方);
        int shinryouId = clinic.addShinryou(shinryouMap.長期処方);
        Scope scope = createScope();
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("true"));
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
        RecordedRequest req = server.takeRequest();
        assertBatchDeleteShinryou(shinryouId, req);
    }

    @Test
    public void tooManyTokutei() throws Exception {
        Clinic clinic = new Clinic();
        clinic.startVisit();
        clinic.addShinryou(shinryouMap.特定疾患処方);
        clinic.addShinryou(shinryouMap.特定疾患処方);
        int shinryouId1 = clinic.addShinryou(shinryouMap.特定疾患処方);
        int shinryouId2 = clinic.addShinryou(shinryouMap.特定疾患処方);
        Scope scope = createScope();
        scope.visits = clinic.getVisits();
        scope.errorHandler = err -> {
            nerror += 1;
            err.getFixFun().run();
        };
        MockWebServer server = getServer();
        server.enqueue(new MockResponse().setBody("true"));
        new CheckChoukiTouyakuKasan(scope).check();
        assertEquals(1, nerror);
        RecordedRequest req = server.takeRequest();
        assertBatchDeleteShinryou(Set.of(shinryouId1, shinryouId2), req);
    }
*/
}
