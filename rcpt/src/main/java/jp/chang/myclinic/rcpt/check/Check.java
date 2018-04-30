package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Check {

    private static Logger logger = LoggerFactory.getLogger(Check.class);
    private boolean fixit = false;
    private int year;
    private int month;

    // check server-url [-f] year month
    public Check(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        Service.setServerUrl(args[1]);
        //Service.setLogBody();
        if (args.length == 5) {
            if (args[2].equals("-f")) {
                fixit = true;
            } else {
                System.err.println("invalid arg : " + args[2]);
                System.exit(1);
            }
            year = Integer.parseInt(args[3]);
            month = Integer.parseInt(args[4]);
        } else if (args.length == 4) {
            year = Integer.parseInt(args[2]);
            month = Integer.parseInt(args[3]);
        } else {
            System.err.println("usage: rcpt check [-f] year month");
            System.exit(1);
        }
    }

    public void run() throws Exception {
        List<Integer> patientIds = Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        for(int patientId: patientIds){
            PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
            System.out.printf("%04d %s%s%n", patient.patientId, patient.lastName, patient.firstName);
            List<VisitFull2DTO> visits = Service.api.listVisitByPatientHavingHokenCall(patientId, year, month)
                    .execute().body();
            List<DiseaseFullDTO> diseases = Service.api.listDiseaseByPatientAtCall(patientId, year, month)
                    .execute().body();
            for(VisitFull2DTO visit: visits){
                new CheckChouki(visit, diseases, fixit).check();
            }
        }
    }

}
