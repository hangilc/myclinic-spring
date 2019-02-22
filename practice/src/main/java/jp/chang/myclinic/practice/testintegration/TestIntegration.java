package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;

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
        new TestShinryou(exam).enterForSimpleExam();
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
