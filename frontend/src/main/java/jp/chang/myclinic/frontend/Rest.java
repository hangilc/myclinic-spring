package jp.chang.myclinic.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rest {

    public static void main(String[] args){
        FrontendRest rest = new FrontendRest();
        System.out.println(rest.getPatient(1).join());
        rest.testVoid().thenAccept(ignore -> System.out.println("void success"));
        FrontendRest.executorService.shutdown();
    }
}
