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
		System.out.println(receptionEnv);
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

    }

}
