package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestIntegration {

    private Map<String, Runnable> testMap = new LinkedHashMap<>();
    {
        testMap.put("cleanup-wqueue", () -> new TestCleanupWqueue().run());
        testMap.put("select-for-exam", () -> new TestSelectForExam().run());
    }

    public void runAll(){
        confirmMockPatient();
        runTests(testMap.keySet());
    }

    public void runTests(Iterable<String> tests){
        confirmMockPatient();
        for(String test: tests){
            Runnable r = testMap.getOrDefault(test, null);
            if( r == null ){
                System.err.printf("Cannot find test: %s\n", test);
                System.exit(1);
            }
            System.out.printf("Started test: %s\n", test);
            r.run();
            System.out.printf("Ended test: %s\n", test);
        }
    }

    private void confirmMockPatient() {
        Service.api.getPatient(1)
                .thenAccept(patient -> {
                    if (!("試験".equals(patient.lastName) && "データ".equals(patient.firstName))) {
                        System.err.println("Invalid mock patient.");
                        System.exit(3);
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    System.err.println("Cannot find mock patient.");
                    System.exit(2);
                    return null;
                });
    }

}
