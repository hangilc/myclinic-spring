package jp.chang.myclinic.intraclinic;

import javax.swing.*;

public class AppIntraclinic
{
    public static void main( String[] args ) {
        if( args.length != 1 ){
            System.out.println("Usage: server-url");
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setLocationByPlatform(true);
        loginDialog.setVisible(true);
    }
}
