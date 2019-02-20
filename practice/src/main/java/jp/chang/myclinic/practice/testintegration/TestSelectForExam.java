package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.SelectFromWqueueDialog;

class TestSelectForExam extends IntegrationTestBase {

    private IntegrationTestHelper helper = new IntegrationTestHelper();

    Exam selectWithNewPatientWithHoken() {
        Exam exam = new Exam();
        exam.patient = helper.enterPatient();
        exam.shahokokuho = helper.enterShahokokuho(exam.patient.patientId);
        exam.visit = helper.startVisit(exam.patient.patientId);
        gui(() -> getMainPane().simulateSelectVisitMenuChoice());
        SelectFromWqueueDialog dialog = waitForWindow(SelectFromWqueueDialog.class);
        gui(() -> {
            boolean ok = dialog.simulateSelectVisit(exam.visit.visitId);
            if (!ok) {
                throw new RuntimeException("select visit failed");
            }
            dialog.simulateSelectButtonClick();
        });
        exam.record = waitForRecord(exam.visit.visitId);
        return exam;
    }

}
