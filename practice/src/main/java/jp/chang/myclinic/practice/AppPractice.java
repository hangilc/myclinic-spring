package jp.chang.myclinic.practice;

import javax.swing.*;
import java.awt.*;

public class AppPractice
{
    public static void main( String[] args )
    {
        if( args.length == 0 ){
            System.out.println("Usage: server-url");
            System.exit(1);
        }
        {
            String serverUrl = args[0];
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
        }
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            MainFrame mainFrame = new MainFrame();
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocationByPlatform(true);
            mainFrame.setVisible(true);
        });
    }
}
