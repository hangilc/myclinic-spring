package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.myclinic.MyclinicConsts.WqueueState;

class MainFrame extends JFrame {

	MainFrame(){
		setTitle("受付");
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		WqueueList wqList = new WqueueList();
		wqList.setPreferredSize(new Dimension(500, 300));
		wqList.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		add(wqList, BorderLayout.CENTER);
		WqueueData[] list = new WqueueData[]{
			new WqueueData(WqueueState.WaitExam, "WAITING EXAM PATIENT"),
			new WqueueData(WqueueState.WaitCashier, "WAITING CASHIER PATIENT")
		};
		wqList.setListData(list);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton closeButton = new JButton("終了");
		closeButton.addActionListener(event -> {
			dispose();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

}