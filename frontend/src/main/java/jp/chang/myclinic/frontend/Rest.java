package jp.chang.myclinic.frontend;

import java.time.LocalDateTime;

public class Rest {

    public static void main(String[] args){
        String serverUrl = "http://localhost:38080/api";
        if( args.length > 0 ){
            serverUrl = args[0];
        }
        FrontendRest rest = new FrontendRest(serverUrl);
        rest.dumpHttp();
        try {
            System.out.println(rest.startVisit(1, LocalDateTime.now()).join());
        } finally {
            rest.shutdown();
        }
    }
}
