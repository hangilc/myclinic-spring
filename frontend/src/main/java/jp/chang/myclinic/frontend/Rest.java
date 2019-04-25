package jp.chang.myclinic.frontend;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Rest {

    public static void main(String[] args){
        FrontendRest rest = new FrontendRest();
        System.out.println(rest.getPatient(1).join());
        FrontendRest.executorService.shutdown();
    }
}
