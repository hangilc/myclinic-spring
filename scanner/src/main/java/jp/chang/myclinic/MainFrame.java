package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

	public MainFrame(){
		setJMenuBar(makeMenuBar());
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

	private JMenuBar makeMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(makeFileMenu());
		menuBar.add(makeSettingMenu());
		return menuBar;
	}

	private JMenu makeFileMenu(){
		JMenu menu = new JMenu("ファイル");
		JMenuItem item;
		item = new JMenuItem("終了");
		item.addActionListener(event -> {
			System.exit(0);
		});
		menu.add(item);
		return menu;
	}

	private JMenu makeSettingMenu(){
		JMenu menu = new JMenu("設定");
		JMenuItem item;
		// item = new JMenuItem("設定ファイル");
		// item.addActionListener(event -> {
		// 	SettingInfoDialog dialog = new SettingInfoDialog(this, "設定ファイル", true);
		// 	dialog.setLocationByPlatform(true);
		// 	dialog.setVisible(true);
		// });
		item = new JMenuItem("保存フォルダー");
		item.addActionListener(event -> {
			ConfigSaveDirDialog dialog = new ConfigSaveDirDialog(this);
			dialog.setLocationByPlatform(true);
			dialog.setVisible(true);
		});
		menu.add(item);
		return menu;
	}
}