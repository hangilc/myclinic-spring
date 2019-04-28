package jp.chang.myclinic.frontend;

public class Rest {

    public static void main(String[] args){
        String serverUrl = "http://localhost:38080/api";
        if( args.length > 0 ){
            serverUrl = args[0];
        }
        FrontendRest rest = new FrontendRest(serverUrl);
        System.out.println(rest.getPatient(1).join());
        rest.shutdown();
    }
}
