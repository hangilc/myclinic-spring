package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.rcpt.check.BatchCheck;
import jp.chang.myclinic.rcpt.create.BatchCreate;
import jp.chang.myclinic.rcpt.data.BatchData;

// TODO: 「訪問看護指示料」、「療養費同意書交付料」の摘要に日付を加える
// TODO: 公費適用時、一部負担金額を記入する。
// TODO: 湿布処方に、１日何枚と摘要欄に加える
// TODO: 「血糖自己測定器加算」、「在宅自己注射指導管理料」に何回分を加える。
// TODO: インスリン処方に何日分を加える。
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
            case "data": {
                BatchData.run(args);
                break;
            }
            case "create": {
                BatchCreate.run(args);
                break;
            }
            default: {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        }
    }

}

