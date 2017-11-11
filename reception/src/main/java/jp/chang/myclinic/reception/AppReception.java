package jp.chang.myclinic.reception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AppReception
{
	private final static Logger logger = LoggerFactory.getLogger(AppReception.class);

	public static void main(String[] args) throws IOException {
		ReceptionArgs receptionArgs = ReceptionArgs.parseArgs(args);
		Service.setServerUrl(receptionArgs.serverUrl);
		ReceptionEnv.INSTANCE.updateWithArgs(receptionArgs);
		Service.api.getClinicInfo()
				.thenAccept(clinicInfo -> {
					ReceptionEnv.INSTANCE.setClinicInfo(clinicInfo);
					startApp();
				});
    }

	private static void startApp(){
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception ex) {
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

}
