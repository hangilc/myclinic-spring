package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.SelectFromWqueueDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestSelectForExam extends IntegrationTestBase implements Runnable {

    private IntegrationTestHelper helper = new IntegrationTestHelper();

    @Override
    public void run() {
        PatientDTO patient = helper.enterPatient();
        helper.enterShahokokuho(patient.patientId);
        VisitDTO visit = helper.startVisit(patient.patientId);
        gui(() -> getMainPane().simulateSelectVisitMenuChoice());
        SelectFromWqueueDialog dialog = waitForWindow(SelectFromWqueueDialog.class);
        gui(() -> {
            boolean ok = dialog.simulateSelectVisit(visit.visitId);
            if( !ok ){
                throw new RuntimeException("select visit failed");
            }
            dialog.simulateSelectButtonClick();
        });
        Record rec = waitForRecord(visit.visitId);
        confirm(rec.getVisitId() == visit.visitId);
    }

}
