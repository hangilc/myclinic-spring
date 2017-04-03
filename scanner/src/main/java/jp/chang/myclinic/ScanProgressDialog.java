package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

class ScanProgressDialog extends JDialog {

	private boolean done = false;
	private JProgressBar progressBar;

	ScanProgressDialog(Dialog owner){
		super(owner, "スキャン実行中...", true);
		add(makeCentralPane(), BorderLayout.CENTER);
		add(makeCommandPane(), BorderLayout.SOUTH);
		pack();
		EventQueue.invokeLater(this::run);
	}

	void start(){
		setVisible(true);
		System.out.println(1);
		System.out.println(2);
		System.out.println(3);
	}

	void run(){
	    int value = 0;
	    while( value < 100 ){
	        try{
	            Thread.sleep(500);
	            value += 10;
	            progressBar.setValue(value);
	            System.out.println(value);
	        } catch(InterruptedException ex){
	            break;
	        }
	    }
	}

	boolean isDone(){
		return done;
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
			dispose();
		});
		panel.add(cancel);
		return panel;
	}

}