package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class SettingInfoDialog extends JDialog {

	SettingInfoDialog(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		setupLabel();
		setupTable();
		setupCommands();
		pack();
	}

	private void setupLabel(){
		JLabel label = new JLabel();
		label.setText(ScannerSetting.INSTANCE.settingFile.toString());
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		add(label, BorderLayout.NORTH);
	}

	private void setupTable(){
		JTable table = new JTable(new Object[][]{
			{"myclinic.scanner.save.dir", ScannerSetting.INSTANCE.settingFile.toString()}
		}, new Object[]{"キー", "値"});
		add(table, BorderLayout.CENTER);
	}

	private void setupCommands(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton closeButton = new JButton("閉じる");
		closeButton.addActionListener(event -> {
			dispose();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

}