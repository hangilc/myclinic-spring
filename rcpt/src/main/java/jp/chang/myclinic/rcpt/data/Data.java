package jp.chang.myclinic.rcpt.data;

import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

class Data {

    private static Logger logger = LoggerFactory.getLogger(Data.class);
    private List<VisitFull2DTO> visits;
    private PrintStream out;

    Data(List<VisitFull2DTO> visits) throws UnsupportedEncodingException {
        this.visits = visits;
        this.out = new PrintStream(System.out, false, "UTF-8");
    }

    void output(){
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<レセプト>");
        out.println("</レセプト>");
    }

}
