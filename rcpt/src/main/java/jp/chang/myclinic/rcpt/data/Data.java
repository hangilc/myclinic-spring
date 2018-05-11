package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.List;

class Data {

    private static Logger logger = LoggerFactory.getLogger(Data.class);
    private int year;
    private int month;
    private List<VisitFull2DTO> visits;
    private PrintStream out;

    Data(PrintStream out, int year, int month) {
        this.out = out;
        this.year = year;
        this.month = month;
    }

    void run() throws Exception {
        List<Integer> patientIds =
                Service.api.listVisitingPatientIdHavingHokenCall(year, month).execute().body();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<レセプト>");
        outProlog();
        out.println("</レセプト>");
    }

    private void outProlog(){

    }

}
