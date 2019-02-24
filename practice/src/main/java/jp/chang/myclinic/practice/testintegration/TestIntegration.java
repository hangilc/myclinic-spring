package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestIntegration extends IntegrationTestBase{

    public TestIntegration(){
        confirmMockPatient();
        new TestCleanupWqueue().run();
    }

    private Map<String, Runnable> tests = new LinkedHashMap<>();
    {
        tests.put("exam", this::testSimpleExam);
        tests.put("text", () -> new TestText().runAll());
    }

    public void runAll(){
        for(String test: tests.keySet()){
            tests.get(test).run();
        }
        System.out.println("Test finished.");
    }

    public void runTest(String test){
        Runnable runnable = tests.getOrDefault(test, null);
        if( runnable == null ){
            System.err.printf("Cannot find test: %s\n", test);
            System.exit(1);
        }
        runnable.run();
        System.out.printf("Test (%s) finished.\n", test);
    }

    private void testSimpleExam(){
        Exam exam = new TestSelectForExam().selectWithNewPatientWithHoken();
        new TestText().enterText(exam.record);
        {
            System.out.printf("texts pane width %f\n", exam.record.getTextsPaneWidth());
        }
        new TestHoken().confirmHokenShahokokuho(exam.record, exam.shahokokuho);
        new TestDrug().enterNaifuku(exam.record);
        new TestShinryou().enterForSimpleExam(exam.record);
        new TestCashier().finishCashier();
        waitForRecordDisappear(exam.record);
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
