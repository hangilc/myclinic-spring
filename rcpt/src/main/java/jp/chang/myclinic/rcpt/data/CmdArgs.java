package jp.chang.myclinic.rcpt.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CmdArgs {

    private static Logger logger = LoggerFactory.getLogger(CmdArgs.class);

    String serverUrl;
    int year;
    int month;

    private static void usage(){
        System.err.println("Usage: java -jar rcpt.jar data [options] server-url year month");
        System.err.println("options:");
        System.err.println("  -h                output help");
    }

    static CmdArgs parse(String[] args){
        if( args.length < 4 ){
            usage();
            System.exit(1);
        }
        CmdArgs cmdArgs = new CmdArgs();
        int i = 1;
        for(;i<args.length;i++){
            String arg = args[i];
            if( !arg.startsWith("-") ){
                break;
            }
            if( arg.length() <= 1 ){
                usage();
                System.exit(1);
            }
            char c = arg.charAt(1);
            switch(c){
                case 'h': {
                    usage();
                    System.exit(0);
                }
                default: {
                    usage();
                    System.exit(1);
                }
            }
        }
        if( args.length - i != 3 ){
            usage();
            System.exit(1);
        }
        cmdArgs.serverUrl = args[i];
        cmdArgs.year = Integer.parseInt(args[++i]);
        cmdArgs.month = Integer.parseInt(args[++i]);
        return cmdArgs;
    }


}
