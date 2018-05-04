package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.mastermap.ShinryouByoumei;
import jp.chang.myclinic.rcpt.check.Check;

import java.util.List;
import java.util.Map;

public class Main {

    //private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args ) throws Exception {
        {
            Map<String, List<ShinryouByoumei>> shinryouByoumeiMap = MasterMap.createShinryouByoumeiMap("./config/shinryou-byoumei.yml");
            System.out.println(shinryouByoumeiMap);
            System.exit(1);
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

