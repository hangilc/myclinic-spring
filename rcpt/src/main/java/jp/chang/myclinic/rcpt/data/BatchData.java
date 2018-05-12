package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BatchData {

    private static Logger logger = LoggerFactory.getLogger(BatchData.class);

    private BatchData() {
    }

    public static void run(String[] args) throws Exception {
        CmdArgs cmdArgs = CmdArgs.parse(args);
        String serverUrl = cmdArgs.serverUrl;
        int year = cmdArgs.year;
        int month = cmdArgs.month;
        Service.setServerUrl(serverUrl);
        PrintWriter out = null;
        String encoding = "UTF-8";
        System.setProperty("line.separator", "\n");
        if (cmdArgs.sjis) {
            encoding = "SJIS";
        }
        out = new PrintWriter(new OutputStreamWriter(System.out, encoding));
        Data data = new Data(out, year, month);
        data.run();
        out.close();
        Service.stop();
    }

}
