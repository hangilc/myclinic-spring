package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.Common;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.time.LocalDate;

public class TestListener extends RunListener {

    public static LocalDate at = LocalDate.of(2018, 3, 1);
    public static Common.MasterMaps masterMaps = Common.getMasterMaps(at);
    public static ResolvedShinryouMap shinryouMap = masterMaps.resolvedMap.shinryouMap;
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
