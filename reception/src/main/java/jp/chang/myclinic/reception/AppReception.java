package jp.chang.myclinic.reception;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppReception
{
	private final static Logger logger = LoggerFactory.getLogger(AppReception.class);

	public static void main(String[] args) throws IOException {
		ReceptionArgs receptionArgs = ReceptionArgs.parseArgs(args);
		Service.setServerUrl(receptionArgs.serverUrl);
		ReceptionEnv receptionEnv = new ReceptionEnv(receptionArgs);
		System.out.println(receptionEnv.getWorkdir().toString());
		System.setProperty("jp.chang.myclinic.reception.workdir", receptionEnv.getWorkdir().toString() + "/");
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			MainFrame mainFrame = new MainFrame(receptionEnv);
			mainFrame.setLocationByPlatform(true);
			mainFrame.setVisible(true);
			mainFrame.doUpdateWqueue();
			new Timer(2000, event -> {
				mainFrame.doUpdateWqueue();
			}).start();
		});

//    	if( args.length == 0 ){
//    		System.out.println("Usage: server-url");
//    		System.exit(1);
//    	}
//    	{
//    		String serverUrl = args[0];
//    		if( !serverUrl.endsWith("/") ){
//    			serverUrl = serverUrl + "/";
//    		}
//            Service.setServerUrl(serverUrl);
//    	}
//    	readConfig();
//        EventQueue.invokeLater(() -> {
//        	try{
//	        	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//	        } catch(Exception ex){
//	        	ex.printStackTrace();
//	        }
//        	MainFrame mainFrame = new MainFrame();
//        	mainFrame.setLocationByPlatform(true);
//        	mainFrame.setVisible(true);
//        	mainFrame.doUpdateWqueue();
//			new Timer(2000, event -> {
//				mainFrame.doUpdateWqueue();
//			}).start();
//        });
    }

    private static Path getDefaultConfigFile(){
    	return Paths.get(System.getProperty("user.dir"), "myclinic-reception.properties");
	}

	private static Path getDefaultPrinterSettingsDir(){
		return Paths.get(System.getProperty("user.dir"));
	}

    private static void readConfig() throws IOException {
//    	Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-reception.properties");
//    	if( Files.exists(configPath) ){
//    		try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
//    			Properties props = new Properties();
//    			props.load(reader);
//				String settingName = props.getProperty("printer-setting-name");
//				if( settingName != null ){
//					ReceptionConfigOld.INSTANCE.setCurrentSetting(settingName);
//				}
//			}
//		} else {
//    		try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
//				Properties props = new Properties();
//				props.setProperty("printer-setting-name", "");
//				props.store(writer, "");
//			}
//		}
	}
}
