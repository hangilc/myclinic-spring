package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.wia.Wia;

public class ScannerApp 
{
    public static void main( String[] args )
    {
    	new ScannerApp(args).run();
    }

    private ScannerApp(String[] args){
    	
    }

    private void run(){
        EventQueue.invokeLater(() -> {
        	Wia.CoInitialize();
        	MainFrame mainFrame = new MainFrame();
        	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	mainFrame.setLocationByPlatform(true);
        	mainFrame.setVisible(true);
        });
    }


}


