package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestIntegration {

    public void runAll(){
        confirmMockPatient();
        new TestCleanupWqueue().run();
        testSimpleExam();
        System.out.println("Test finished.");
    }

    public void runTests(Iterable<String> tests){
        confirmMockPatient();
        throw new RuntimeException("Not implemented");
    }

    private void testSimpleExam(){
        Exam exam = new TestSelectForExam().selectWithNewPatientWithHoken();
        new TestText(exam).runAll();
        new TestHoken(exam).confirmHokenShahokokuho(exam.shahokokuho);
        new TestDrug(exam).enterNaifuku();
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
