package jp.chang.myclinic.intraclinic;

public class AppIntraclinic
{
    public static void main( String[] args ) {
        if( args.length != 2 ){
            System.out.println("Usage: server-url user");
            System.exit(1);
        }
        {
            String serverUrl = args[0];
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            serverUrl += "/intraclinic/";
            Service.setServerUrl(serverUrl);
        }
        String user = args[1];

    }
}
