package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Data data = new Data(year, month, cmdArgs.patientIds, cmdArgs.includeNoHoken);
        data.run();
        Service.stop();
    }

}
