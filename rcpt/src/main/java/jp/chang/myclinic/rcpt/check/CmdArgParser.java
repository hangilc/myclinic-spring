package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CmdArgParser {

    private CmdArgParser() { }

    private static void usage() {
        System.err.println("usage: rcpt check [options] serverUrl year month");
        System.err.println("  options:");
        System.err.println("    -f                 : fix problems");
        System.err.println("    -p=1234,3211,...   : handle only specified patientIds");
        System.err.println("    -v                 : verbose");
        System.err.println("    -h                 : help");
    }

    static RunEnv parse(String[] args) throws IOException {
        RunEnv env = new RunEnv();
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
                        env.fixit = true;
                        break;
                    case 'p':
                        env.patientIds = optPatientIds(s.substring(2));
                        break;
                    case 'v':
                        env.verbose = true;
                        break;
                    case 'h':
                        usage();
                        System.exit(0);
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
        try {
            env.year = Integer.parseInt(args[i + 1]);
            env.month = Integer.parseInt(args[i + 2]);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid year or month.");
            usage();
            System.exit(1);
        }
        String serverUrl = args[i];
        Service.setServerUrl(serverUrl);
        env.api = new FixerService(Service.api);
        if( env.patientIds == null ){
            env.patientIds = Service.api.listVisitingPatientIdHavingHokenCall(env.year, env.month).execute().body();
        }
        return env;
    }

    private static List<Integer> optPatientIds(String arg) {
        List<Integer> optPatientIds = new ArrayList<>();
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
        return optPatientIds;
    }

}
