package jp.chang.myclinic.hotline;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

// TODO: optionally show others hotlines
public class AppHotline
{
    public static void main( String[] args ) {
        if( args.length != 3 ){
            System.out.println("Usage: server-url sender recipient");
            System.exit(1);
        }
        {
            String serverUrl = args[0];
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
        }
        String sender = args[1];
        String recipient = args[2];
        {
            List<String> validRoles = Arrays.asList("Reception", "Pharma", "Practice");
            if( !validRoles.contains(sender) ){
                System.err.println("invalid sender (should be one of Reception, Pharma, or Practice");
                System.exit(2);
            }
            if( !validRoles.contains(recipient) ){
                System.err.println("invalid recipient (should be one of Reception, Pharma, or Practice");
                System.exit(2);
            }
        }
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            MainFrame mainFrame = new MainFrame(sender, recipient);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocationByPlatform(true);
            mainFrame.setVisible(true);
            Reloader reloader = new Reloader(mainFrame);
            Thread th = new Thread(reloader);
            th.setDaemon(true);
            th.start();
            mainFrame.setReloader(reloader);
        });

    }
}
