package jp.chang.myclinic.hotline;

import javax.swing.*;
import java.awt.*;

// TODO: optionally show others hotlines
public class AppHotline
{
    public static void main( String[] args ) {
        if( args.length != 3 ){
            System.out.println("Usage: server-url sender recipient");
            System.out.println("ssender/receipient should be one of practice, pharmacy, or reception");
            System.exit(1);
        }
        {
            String serverUrl = args[0];
            if( !serverUrl.endsWith("/") ){
                serverUrl = serverUrl + "/";
            }
            Service.setServerUrl(serverUrl);
        }
//        String sender = args[1];
//        String recipient = args[2];
        User senderUser = resolveUser(args[1]);
        User recipientUser = resolveUser(args[2]);
//        {
//            List<String> validRoles = Arrays.asList("Reception", "Pharmacy", "Practice");
//            if( !validRoles.contains(sender) ){
//                System.err.println("invalid sender (should be one of Reception, Pharmacy, or Practice");
//                System.exit(2);
//            }
//            if( !validRoles.contains(recipient) ){
//                System.err.println("invalid recipient (should be one of Reception, Pharmacy, or Practice");
//                System.exit(2);
//            }
//        }
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            MainFrame mainFrame = new MainFrame(senderUser, recipientUser);
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

    private static User resolveUser(String arg){
        for(User user: User.values()){
            if( user.getName().equalsIgnoreCase(arg) ){
                return user;
            }
        }
        throw new RuntimeException("invalid user name: " + arg);
    }
}
