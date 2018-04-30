package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.rcpt.check.Check;

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
                new Check(args).run();
                break;
            }
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

}

