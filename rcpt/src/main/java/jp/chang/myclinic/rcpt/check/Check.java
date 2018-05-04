package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Check {

    //private static Logger logger = LoggerFactory.getLogger(Check.class);
    private String serverUrl;
    private boolean fixit = false;
    private int year;
    private int month;
    private List<Integer> optPatientIds;

    public Check(String[] args) {
        parseArgs(args);
    }

    private void usage() {
        System.err.println("usage: rcpt check [options] serverUrl year month");
        System.err.println("  options:");
        System.err.println("    -f                 : fix problems");
        System.err.println("    -p=1234,3211,...   : handle only specified patientIds");
    }

    private void parseArgs(String[] args) {
        if (args.length < 4) {
            usage();
            System.exit(1);
        }
        int i = 1;
        while (i < args.length) {
            String s = args[i];
            if (s.startsWith("-")) {
                if (s.length() < 2) {
                    usage();
                    System.exit(1);
                }
                char opt = s.charAt(1);
                switch (opt) {
                    case 'f':
                        fixit = true;
                        break;
                    case 'p':
                        optPatientIds(s.substring(2));
                        break;
                    default:
                        usage();
                        System.exit(1);
                        break;
                }
                i += 1;
            } else {
                break;
            }
        }
        if (args.length - i != 3) {
            usage();
            System.exit(1);
        }
        serverUrl = args[i];
        try {
            year = Integer.parseInt(args[i + 1]);
            month = Integer.parseInt(args[i + 2]);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid year or month.");
            usage();
            System.exit(1);
        }
    }

    private void optPatientIds(String arg) {
        this.optPatientIds = new ArrayList<>();
        if (arg.startsWith("=")) {
            arg = arg.substring(1);
        }
        String[] toks = arg.split(",");
        for (String tok : toks) {
            try {
                int patientId = Integer.parseInt(tok);
                optPatientIds.add(patientId);
            } catch (NumberFormatException ex) {
                System.err.println("Invalid patientId");
                System.exit(1);
            }
        }
    }

    public void run() throws Exception {
        Service.setServerUrl(serverUrl);
        Common.MasterMaps masterMaps = Common.getMasterMaps(LocalDate.of(year, month, 1));
        List<Integer> patientIds;
        if (optPatientIds != null) {
            patientIds = optPatientIds;
        } else {
            patientIds = Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        }
        for (int patientId : patientIds) {
            PatientDTO patient = Service.api.getPatientCall(patientId).execute().body();
            System.out.printf("%04d %s%s%n", patient.patientId, patient.lastName, patient.firstName);
            List<VisitFull2DTO> visits = Service.api.listVisitByPatientHavingHokenCall(patientId, year, month)
                    .execute().body();
            assert visits.size() > 0;
            List<DiseaseFullDTO> diseases = Service.api.listDiseaseByPatientAtCall(patientId, year, month)
                    .execute().body();
            if (diseases == null) {
                System.err.println("Failed to get disease list (some checks skipped). PatientID " + patientId);
                continue;
            }
            Scope scope = new Scope(visits, masterMaps.resolvedMap, masterMaps.shinryouByoumeiMap, diseases);
            new CheckChouki(scope).check(fixit);
            new CheckTokuteiShikkanKanri(scope).check(fixit);
            new CheckChoukiTouyakuKasan(scope).check(fixit);
            new CheckHandanryou(scope).check(fixit);
            new CheckShoshinSaisin(scope).check(fixit);
            new CheckKouseishinyaku(scope).check(fixit);
            new CheckGaiyou(scope).check(fixit);
            new CheckShohouryou(scope).check(fixit);
            new CheckDiseaseExists(scope).check(fixit);
            new CheckShoshinByoumei(scope).check(fixit);
            new CheckSaishinByoumei(scope).check(fixit);
            new CheckByoumei(scope).check(fixit);
        }
        Service.stop();
    }

}
