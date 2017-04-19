package jp.chang.myclinic;


import java.awt.*;
import javax.swing.*;
import java.nio.file.Path;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigDpiDialog extends JDialog {

	private int dpi = ScannerSetting.INSTANCE.dpi;
	private JLabel dpiLabel = new JLabel(String.valueOf(dpi));
	private Logger logger = LoggerFactory.getLogger(ConfigDpiDialog.class);

	ConfigDpiDialog(JFrame owner){
		super(owner, "ＤＰＩの設定", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(dpiLabel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(centerCommand());
		add(panel, BorderLayout.CENTER);
	}

	private JComponent centerCommand(){
		JPanel panel = new JPanel();
		JButton browseButton = new JButton("変更");
		browseButton.addActionListener(event -> {
			String input = JOptionPane.showInputDialog(this, "DPIの値の入力");
			if( input != null ){
				try{
					int ival = Integer.parseInt(input);
					changeDpi(ival);
				} catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(this, "数値の入力が不適切です。");
				}
			}
		});
		panel.add(browseButton);
		panel.add(Box.createHorizontalGlue());
		return panel;
	}

	private void changeDpi(int ival){
		dpi = ival;
		dpiLabel.setText(String.valueOf(dpi));
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
			ScannerSetting.INSTANCE.dpi = dpi;
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