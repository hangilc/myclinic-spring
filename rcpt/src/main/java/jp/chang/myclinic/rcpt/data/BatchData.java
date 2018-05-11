package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        List<Integer> patientIds =
                Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        System.out.println(patientIds);
        Service.stop();
    }

}
