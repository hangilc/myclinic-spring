package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppPharma {
    public static void main(String[] args) throws IOException {
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
        //readConfig();
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            MainFrame mainFrame = new MainFrame();
            mainFrame.setSize(new Dimension(600, 400));
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocationByPlatform(true);
            mainFrame.setVisible(true);
        });
    }

//    private static void readConfig() throws IOException {
//        Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-pharma.properties");
//        if( Files.exists(configPath) ){
//            try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
//                Properties props = new Properties();
//                props.load(reader);
//                String settingName = props.getProperty("printer-setting-name");
//                if( settingName != null ){
//                    PharmaConfig.INSTANCE.setPrinterSetting(settingName);
//                }
//            }
//        } else {
//            try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
//                Properties props = new Properties();
//                props.setProperty("printer-setting-name", "");
//                props.store(writer, "");
//            }
//        }
//    }


}
