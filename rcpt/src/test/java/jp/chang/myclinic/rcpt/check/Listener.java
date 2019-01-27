package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.rcpt.Common;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedMap;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedShinryouMap;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.time.LocalDate;

public class Listener extends RunListener {

    public static LocalDate at = LocalDate.of(2018, 3, 1);
    public static ResolvedMap resolvedMap;
    public static ResolvedShinryouMap shinryouMap;
    {
        Service.setServerUrl(System.getenv("MYCLINIC_SERVICE"));
        resolvedMap = Common.getMasterMaps(at).join();
        shinryouMap = resolvedMap.shinryouMap;
    }
    public static ObjectMapper objectMapper;

    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println("Test Started...");
        objectMapper = new ObjectMapper();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println("Test Ended...");
    }

}
