package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ScannerDialog extends JDialog {

	private boolean canceled = true;
	private ScanTask task;
	private static Logger logger = LoggerFactory.getLogger(ScannerDialog.class);

	ScannerDialog(Dialog owner, String deviceId, Path savePath){
		super(owner, "スキャン実行中", true);
		this.task = new ScanTask(this, deviceId, savePath);
	}

	public boolean isCanceled(){
		return canceled;
	}

	private void setCanceled(boolean canceled){
		this.canceled = canceled;
	}

	@Override
	public void setVisible(boolean visible){
		System.out.println("set visible: " + visible);
		if( visible ){
			Thread thr = new Thread(task);
			thr.setDaemon(true);
			thr.start();
		} else {
			task.setCanceled(true);
		}
		super.setVisible(visible);
	}

	public void onError(String message, Exception ex){
		logger.error("exception while scanning {}", message, ex);
		JOptionPane.showMessageDialog(this, message);
		setVisible(false);
		dispose();
	}

	public void onSuccess(){
		setCanceled(false);
		setVisible(false);
		dispose();
	}

	public void onProgress(int percent){
		System.out.println("PERCENT: " + percent);
	}

}