package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

class ScanProgressDialog extends JDialog {

	private boolean canceled = false;
	private JProgressBar progressBar;

	ScanProgressDialog(Dialog owner){
		super(owner, "スキャン実行中...", true);
		add(makeCentralPane(), BorderLayout.CENTER);
		add(makeCommandPane(), BorderLayout.SOUTH);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		pack();
	}

	boolean isCanceled(){
		return canceled;
	}

	void setValue(int value){
		progressBar.setValue(value);
	}

	private JComponent makeCentralPane(){
		progressBar = new JProgressBar(0, 100);
		return progressBar;
	}

	private JComponent makeCommandPane(){
		JPanel panel = new JPanel();
		JButton cancel = new JButton("キャンセル");
		cancel.addActionListener(event -> {
			canceled = true;
		});
		panel.add(cancel);
		return panel;
	}

}