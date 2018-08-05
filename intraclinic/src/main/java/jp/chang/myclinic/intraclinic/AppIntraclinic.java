package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostFullPageDTO;
import jp.chang.myclinic.dto.UserInfoDTO;

import javax.swing.*;
import java.awt.*;

public class AppIntraclinic {
    public static void main(String[] args) throws Exception {
        String serverUrl = null;
        if (args.length == 0) {
            serverUrl = System.getenv("MYCLINIC_SERVICE");
        } else if (args.length == 1) {
            serverUrl = args[0];
        } else {
            System.out.println("Usage: [server-url]");
            System.exit(1);
        }
        if (serverUrl == null || serverUrl.isEmpty()) {
            throw new RuntimeException("Cannot find server url.");
        }
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        serverUrl += "/intraclinic/";
        Service.setServerUrl(serverUrl);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(2);
//        }
        LoginDialog loginDialog = new LoginDialog(userInfo -> {
            Service.api.listPost(0)
                    .thenAccept(page -> {
                        EventQueue.invokeLater(() -> openMainWindow(userInfo, page));
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(null, t.toString()));
                        return null;
                    });
        });
        loginDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginDialog.setLocationByPlatform(true);
        loginDialog.setVisible(true);
    }

    private static void openMainWindow(UserInfoDTO userInfo, IntraclinicPostFullPageDTO page) {
        if (userInfo != null && userInfo.roles != null) {
            boolean isAdmin = userInfo.roles.contains("admin");
            MainWindow mainWindow = new MainWindow(isAdmin, userInfo.name, page);
            mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainWindow.setLocationByPlatform(true);
            mainWindow.setVisible(true);
        } else {
            System.exit(0);
        }
    }

}
