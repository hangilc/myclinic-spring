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
}
