package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.rcpt.check.BatchCheck;
import jp.chang.myclinic.rcpt.create.BatchCreate;
import jp.chang.myclinic.rcpt.data.BatchData;

import java.time.LocalDate;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) throws Exception {
        {
            Service.setServerUrl(System.getenv("MYCLINIC_SERVICE"));
            NewCommon.getMasterMaps(LocalDate.now());
        }
        if( args.length < 1 ){
            System.err.println("usage: rcpt command ...");
            System.exit(1);
        }
        String command = args[0];
        switch(command){
            case "check": {
                BatchCheck.run(args);
                break;
            }
            case "data": {
                BatchData.run(args);
                break;
            }
            case "create": {
                BatchCreate.run(args);
                break;
            }
            case "newcreate": {
                if( args.length != 2 ){
                    System.err.println("Usage: newcreate DATA-XML-FILE");
                    System.exit(1);
                }
                jp.chang.myclinic.rcpt.newcreate.Create.run(args[1], System.out);
                break;
            }
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

}

