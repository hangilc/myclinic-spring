package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.wia.Wia;

public class ScannerApp 
{
    public static void main( String[] args )
    {
        EventQueue.invokeLater(() -> {
        	Wia.CoInitialize();
        	MainFrame mainFrame = new MainFrame();
        	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	mainFrame.setLocationByPlatform(true);
        	mainFrame.setVisible(true);
        });
    }
}


