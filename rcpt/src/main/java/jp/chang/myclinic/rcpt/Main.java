package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap.ResolvedMap;
import jp.chang.myclinic.rcpt.check.Check;

import java.time.LocalDate;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) throws Exception {
        {
            ResolvedMap m = Common.getResolvedMap(LocalDate.now());
            System.out.println(m.shinryouMap.免疫検査判断料);
            System.exit(0);
        }


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

