package jp.chang.myclinic.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class CmdOpts {

    //private static Logger logger = LoggerFactory.getLogger(CmdOpts.class);
    private Integer mgmtPort = null;
    private String[] remainingArgs;

    private CmdOpts() {

    }

    private static void usage(){
        System.out.println("usage: reception [options] [server-url]");
        System.out.println("  options:");
        System.out.println("    -management management-port");
    }

    static CmdOpts parse(String[] args){
        CmdOpts opts = new CmdOpts();
        int i=0;
        for(;i<args.length;i++){
            String opt = args[i];
            if( !opt.startsWith("--") ){
                break;
            }
            switch(opt){
                case "--management": {
                    if( i + 1 >= args.length ){
                        System.err.println("Missing arg to -management option");
                        usage();
                        System.exit(1);
                    }
                    String arg = args[++i];
                    try {
                        opts.mgmtPort = Integer.parseInt(arg);
                    } catch(NumberFormatException ex){
                        System.err.println("Invalid number argument to -management option");
                        usage();
                        System.exit(1);
                    }
                    break;
                }
                default: {
                    System.err.printf("Invalid option %s\n", opt);
                    usage();
                    System.exit(1);
                }
            }
        }
        opts.remainingArgs = Arrays.copyOfRange(args, i, args.length);
        return opts;
    }

    public boolean hasMgmtPort(){
        return mgmtPort != null;
    }

    public int getMgmtPort(){
        return mgmtPort;
    }

    public String[] getRemainingArgs(){
        return remainingArgs;
    }

}
