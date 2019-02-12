package jp.chang.myclinic.practice.selftest;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.Globals;
import jp.chang.myclinic.practice.javafx.MainPane;

public class PracticeSelfTest implements Runnable {

    //private static Logger logger = LoggerFactory.getLogger(PracticeSelfTest.class);
    private MockData mocker = new MockData();
    private MainPane mainPane;

    @Override
    public void run(){
        System.out.println("Self-test started");
        confirmMockPatient();
        System.out.println("Confirmed that mock database is connected.");
        this.mainPane = Globals.getInstance().getMainPane();
        testExam();
        System.out.println("Self-test completed without error.");
    }

    private void testExam(){
        PatientDTO patient = createNewPatient();
    }

    private PatientDTO createNewPatient(){
        PatientDTO mockPatient = mocker.pickPatient();
        int patientId = Service.api.enterPatient(mockPatient).join();
        mockPatient.patientId = patientId;
        return mockPatient;
    }

    private ShahokokuhoDTO createShahokokuho(int patientId){
        ShahokokuhoDTO hoken =
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
