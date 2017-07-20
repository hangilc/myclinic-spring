package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.UserInfoDTO;

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
        loginDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginDialog.setLocationByPlatform(true);
        loginDialog.setVisible(true);
        UserInfoDTO userInfo = loginDialog.getUserInfo();
        if( userInfo != null ){
            System.out.println(userInfo);
        }
    }
}
