package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.time.LocalDate;

public class TestListener extends RunListener {

    public static MockWebServer server;
    public static LocalDate at = LocalDate.of(2018, 3, 1);
    public static Common.MasterMaps masterMaps = Common.getMasterMaps(at);
    public static ResolvedShinryouMap shinryouMap = masterMaps.resolvedMap.shinryouMap;


    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println("Test Started...");
        server = new MockWebServer();
        server.start();
        Service.setServerUrl(server.url("/json").toString());
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        Service.stop();
        server.shutdown();
        System.out.println("Test Ended...");
    }

}
