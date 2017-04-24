package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;

public class AppReception 
{
    public static void main( String[] args ) throws IOException {
        System.out.println(Service.listWqueue());
        EventQueue.invokeLater(() -> {
        	try{
	        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        }
        	MainFrame mainFrame = new MainFrame();
        	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	mainFrame.setLocationByPlatform(true);
        	mainFrame.setVisible(true);
        });
    }
}
