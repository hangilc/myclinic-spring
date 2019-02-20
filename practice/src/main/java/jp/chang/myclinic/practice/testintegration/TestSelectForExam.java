package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestSelectForExam extends IntegrationTestBase implements Runnable {

    private IntegrationTestHelper helper = new IntegrationTestHelper();

    @Override
    public void run() {
//        PatientDTO patient = helper.enterPatient();
//        ShahokokuhoDTO hoken = helper.enterShahokokuho(patient.patientId);
//        VisitDTO visit = helper.startVisit(patient.patientId);
        getMainPane().simulateSelectVisitMenuChoice();
    }
}
