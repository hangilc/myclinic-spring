package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BatchCheck {

    //private static Logger logger = LoggerFactory.getLogger(BatchCheck.class);

    private BatchCheck() { }

    public static void run(String[] args) throws IOException {
        RunEnv runEnv = CmdArgParser.parse(args);
        class State {
            private int prevPatientId = 0;
        }
        State state = new State();
        runEnv.errorHandler = error -> {
            if( !runEnv.verbose ){
                PatientDTO patient = error.getPatient();
                if( state.prevPatientId != patient.patientId ) {
                    System.out.printf("%04d %s%s%n", patient.patientId, patient.lastName, patient.firstName);
                    state.prevPatientId = patient.patientId;
                }
            }
            System.out.println(error.getMessage());
            if( runEnv.fixit ){
                String fixMessage = error.getFixMessage();
                if( fixMessage != null ){
                    System.out.println(fixMessage);
                }
                Runnable fixer = error.getFixFun();
                if( fixer != null ){
                    fixer.run();
                    System.out.println("FIXED");
                }
            }
        };
        Check.run(runEnv);
        Service.stop();
    }
}
