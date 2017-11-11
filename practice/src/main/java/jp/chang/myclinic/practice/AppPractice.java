package jp.chang.myclinic.practice;

import javax.swing.*;
import java.awt.*;

public class AppPractice
{
    public static void main( String[] args )
    {
        CommandArgs commandArgs = new CommandArgs(args);
        Service.setServerUrl(commandArgs.getServerUrl());
        PracticeEnv.INSTANCE = new PracticeEnv(commandArgs);
        Service.api.getClinicInfo()
                .thenAccept(clinicInfo -> {
                    PracticeEnv.INSTANCE.setClinicInfo(clinicInfo);
                    startApp();
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private static void startApp(){
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

    private static void alert(String message){
        JOptionPane.showMessageDialog(null, message);
    }

}
