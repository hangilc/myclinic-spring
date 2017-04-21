package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.nio.file.Path;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigDeviceDialog extends JDialog {

	private String device = ScannerSetting.INSTANCE.defaultDevice;
	private JLabel deviceLabel = new JLabel(device + " ");
	private static Logger logger = LoggerFactory.getLogger(ConfigDeviceDialog.class);

	ConfigDeviceDialog(JFrame owner){
		super(owner, "スキャナーデバイスの設定", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(deviceLabel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(centerCommand());
		add(panel, BorderLayout.CENTER);
	}

	private JComponent centerCommand(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton browseButton = new JButton("変更");
		browseButton.addActionListener(event -> {
			String newVal = ScannerUtil.pickDevice();
			if( newVal != null ){
				changeDevice(newVal);
			}
		});
		panel.add(browseButton);
		JButton clearButton = new JButton("クリア");
		clearButton.addActionListener(event -> {
			changeDevice("");
		});
		panel.add(clearButton);
		return panel;
	}

	private void changeDevice(String newVal){
		device = newVal;
		deviceLabel.setText(device + " ");
		pack();
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JCheckBox saveCheckBox = new JCheckBox("設定ファイルに保存する");
		saveCheckBox.setSelected(true);
		JButton enterButton = new JButton("決定");
		JButton cancelButton = new JButton("キャンセル");
		enterButton.addActionListener(event -> {
			ScannerSetting.INSTANCE.defaultDevice = device;
			if( saveCheckBox.isSelected() ){
				try{
					ScannerSetting.INSTANCE.saveToFile();
				} catch(IOException ex){
					JOptionPane.showMessageDialog(null, "設定ファイルへの保存に失敗しました。");
					logger.error("failed to save to file", ex);
				}
			}
			dispose();
		});
		cancelButton.addActionListener(event -> {
			dispose();
		});
		panel.add(Box.createHorizontalGlue());
		panel.add(saveCheckBox);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(enterButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cancelButton);
		add(panel, BorderLayout.SOUTH);
	}

}