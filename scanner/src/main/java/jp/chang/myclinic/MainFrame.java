package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

	public MainFrame(){
		JPanel panel = new JPanel();
		JButton patient = new JButton("患者書類");
		JButton general = new JButton("一般書類");
		general.addActionListener(event -> {
			GeneralDocScanner docScanner = new GeneralDocScanner(this);
			docScanner.setLocationByPlatform(true);
			docScanner.setVisible(true);
		});
		panel.add(patient);
		panel.add(general);
		add(panel, BorderLayout.CENTER);
		pack();
	}

}