package jp.chang.myclinic.integraltest;

import jp.chang.myclinic.client.Service;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        new Main().run(args);
    }

    private void usage(){
        System.out.println("Usage: integral-test SERVER-URL");
    }

    private void run(String[] args){
        if( args.length != 1 ){
            usage();
            System.exit(1);
        }
        String serverUrl = args[0];
        Service.setServerUrl(serverUrl);
        confirmMockPatient();
        Service.stop();
    }

    private void confirmMockPatient(){
        Service.api.getPatient(1)
                .thenAccept(patient -> {
                    if( !("試験".equals(patient.lastName) && "データ".equals(patient.firstName)) ){
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
