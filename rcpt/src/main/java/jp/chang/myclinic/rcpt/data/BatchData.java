package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class BatchData {

    private static Logger logger = LoggerFactory.getLogger(BatchData.class);

    private BatchData() { }

    public static void run(String[] args) throws Exception {
        CmdArgs cmdArgs = CmdArgs.parse(args);
        String serverUrl = cmdArgs.serverUrl;
        int year = cmdArgs.year;
        int month = cmdArgs.month;
        Service.setServerUrl(serverUrl);
        PrintStream out;
        if( cmdArgs.sjis ){
            this.out = new PrintStream(System.out, false);
        } else {
            this.out = new PrintStream(System.out, false, "UTF-8");
        }
        Data data = new Data(out, year, month);
        data.run();
        Service.stop();
    }

}
