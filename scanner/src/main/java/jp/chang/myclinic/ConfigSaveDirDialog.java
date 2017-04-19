package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.nio.file.Path;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigSaveDirDialog extends JDialog {

	private boolean canceled = true;
	private Path saveDir = ScannerSetting.INSTANCE.savingDir;
	private JLabel saveDirLabel = new JLabel(saveDir.toString());
	private static Logger logger = LoggerFactory.getLogger(ConfigSaveDirDialog.class);

	ConfigSaveDirDialog(JFrame owner){
		super(owner, "保存先フォルダーの設定", true);
		setupCenter();
		setupSouth();
		pack();
	}

	public boolean isCanceled(){
		return canceled;
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(saveDirLabel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(centerCommand());
		add(panel, BorderLayout.CENTER);
	}

	private JComponent centerCommand(){
		JPanel panel = new JPanel();
		JButton browseButton = new JButton("参照");
		browseButton.addActionListener(event -> {
			JFileChooser jfc = new JFileChooser(saveDir.toFile());
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = jfc.showDialog(this, "選択");
			if( ret == JFileChooser.APPROVE_OPTION ){
				changeCurrentPath(jfc.getSelectedFile().toPath());
			}
		});
		panel.add(browseButton);
		panel.add(Box.createHorizontalGlue());
		return panel;
	}

	private void changeCurrentPath(Path newPath){
		saveDir = newPath;
		saveDirLabel.setText(saveDir.toString());
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JCheckBox saveCheckBox = new JCheckBox("設定ファイルに保存する");
		JButton enterButton = new JButton("決定");
		JButton cancelButton = new JButton("キャンセル");
		saveCheckBox.setSelected(true);
		enterButton.addActionListener(event -> {
			canceled = false;
			ScannerSetting.INSTANCE.savingDir = saveDir;
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