package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.rcpt.check.BatchCheck;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) throws Exception {
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
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

}

