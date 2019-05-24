package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.rcpt.check.BatchCheck;
import jp.chang.myclinic.rcpt.data.BatchData;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedDiseaseAdjMap;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedDiseaseMap;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedKizaiMap;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedShinryouMap;

import java.time.LocalDate;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Service.setLogBody();
        Service.setServerUrl(System.getenv("MYCLINIC_SERVICE"));
        if (args.length < 1) {
            System.err.println("usage: rcpt command ...");
            System.exit(1);
        }
        String command = args[0];
        switch (command) {
            case "check": {
                BatchCheck.run(args);
                break;
            }
            case "data": {
                BatchData.run(args);
                break;
            }
            case "create": {
                if (args.length != 2) {
                    System.err.println("Usage: newcreate DATA-XML-FILE");
                    System.exit(1);
                }
                jp.chang.myclinic.rcpt.create.Create.run(args[1], System.out);
                break;
            }
            case "test-resolve": {
                LocalDate at = LocalDate.parse(args[1]);
                ResolvedShinryouMap s = new ResolvedShinryouMap();
                ResolvedKizaiMap k = new ResolvedKizaiMap();
                ResolvedDiseaseMap b = new ResolvedDiseaseMap();
                ResolvedDiseaseAdjMap a = new ResolvedDiseaseAdjMap();
                s.resolveAt(at)
                        .thenCompose(v -> {
                            System.out.println(s);
                            System.out.printf("Unresolved shinryou: %s\n", s.getUnresolved().toString());
                            return k.resolveAt(at);
                        })
                        .thenCompose(v -> {
                            System.out.println(k);
                            System.out.printf("Unresolved kizai: %s\n", k.getUnresolved().toString());
                            return b.resolveAt(at);
                        })
                        .thenCompose(v -> {
                            System.out.println(b);
                            System.out.printf("Unresolved byoumei: %s\n", b.getUnresolved().toString());
                            return a.resolveAt(at);
                        })
                        .thenAccept(v -> {
                            System.out.println(a);
                            System.out.printf("Unresolved shuushokugo: %s\n", a.getUnresolved().toString());
                            System.exit(0);
                        });
                break;
            }
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

}

