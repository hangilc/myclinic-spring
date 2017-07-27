package jp.chang.myclinic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

// TODO: decrease height of patient list
public class AppReception 
{
    public static void main( String[] args ) throws IOException {
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
    	readConfig();
        EventQueue.invokeLater(() -> {
        	try{
	        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        } catch(Exception ex){
	        	ex.printStackTrace();
	        }
        	MainFrame mainFrame = new MainFrame();
        	mainFrame.setLocationByPlatform(true);
        	mainFrame.setVisible(true);
        	mainFrame.doUpdateWqueue();
			new Timer(2000, event -> {
				mainFrame.doUpdateWqueue();
			}).start();
        });
    }

    private static void readConfig() throws IOException {
    	Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-reception.properties");
    	if( Files.exists(configPath) ){
    		try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
    			Properties props = new Properties();
    			props.load(reader);
				String settingName = props.getProperty("printer-setting-name");
				if( settingName != null ){
					ReceptionConfig.INSTANCE.setCurrentSetting(settingName);
				}
			}
		} else {
    		try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
				Properties props = new Properties();
				props.setProperty("printer-setting-name", "");
				props.store(writer, "");
			}
		}
	}
}
