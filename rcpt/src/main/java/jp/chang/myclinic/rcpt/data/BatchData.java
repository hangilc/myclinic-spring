package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class BatchData {

    private static Logger logger = LoggerFactory.getLogger(BatchData.class);

    private BatchData() { }

    // Usage: java -jar rcpt.jar data SERVER-URL YEAR MONTH
    public static void run(String[] args) throws Exception {
        if( args.length != 4 ){
            System.err.println("Usage: java -jar rcpt.jar server-url year month");
            System.exit(1);
        }
        String serverUrl = args[1];
        int year = Integer.parseInt(args[2]);
        int month = Integer.parseInt(args[3]);
        Service.setServerUrl(serverUrl);
        Data data = new Data(year, month);
        data.run();
        PrintStream out = new PrintStream(System.out, false, "UTF-8");
        Service.stop();
    }

}
