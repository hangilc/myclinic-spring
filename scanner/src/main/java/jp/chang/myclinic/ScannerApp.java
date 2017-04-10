package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.wia.Wia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScannerApp 
{
    private Logger logger = LoggerFactory.getLogger(ScannerApp.class);

    public static void main( String[] args )
    {
    	new ScannerApp(args).run();
    }

    private ScannerApp(String[] args){
    	logger.info("App started");
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


