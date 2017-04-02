package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

	public MainFrame(){
		JPanel panel = new JPanel();
		JButton patient = new JButton("患者書類");
		JButton general = new JButton("一般書類");
		patient.addActionListener(event -> {
			String patientIdInput = JOptionPane.showInputDialog(this, "患者番号を入力してください。");
			Integer patientId = null;
			while( true ){
				if( patientIdInput == null ){
					break;
				}
				try {
					patientId = Integer.parseInt(patientIdInput);
					break;
				} catch(NumberFormatException ex){
					patientIdInput = JOptionPane.showInputDialog(this, 
						"患者番号を入力してください。\n（患者番号の値が不適切です。）", patientIdInput);
				}
			}
			if( patientId == null ){
				return;
			}
			PatientDocScanner docScanner = new PatientDocScanner(this, patientId);
			docScanner.setLocationByPlatform(true);
			docScanner.setVisible(true);
		});
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