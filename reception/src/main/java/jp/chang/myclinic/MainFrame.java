package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.myclinic.MyclinicConsts.WqueueState;

class MainFrame extends JFrame {

	MainFrame(){
		setTitle("受付");
		setupNorth();
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupNorth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel upperBox = new JPanel();
		upperBox.setLayout(new BoxLayout(upperBox, BoxLayout.LINE_AXIS));
		JPanel lowerBox = new JPanel();
		lowerBox.setLayout(new BoxLayout(lowerBox, BoxLayout.LINE_AXIS));
		{
			JButton newPatientButton = new JButton("新規患者");
			JButton searchPatientButton = new JButton("患者検索");
			JButton searchCashierButton = new JButton("会計検索");
			JButton receiptButton = new JButton("領収証用紙");

			upperBox.add(newPatientButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(searchPatientButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(searchCashierButton);
			upperBox.add(Box.createHorizontalStrut(30));
			upperBox.add(receiptButton);
			upperBox.add(Box.createHorizontalGlue());
		}
		{
			JTextField patientIdField = new JTextField(6);
			patientIdField.setMaximumSize(patientIdField.getPreferredSize());
			JButton registerButton = new JButton("診療受付");
			JButton patientInfoButton = new JButton("患者情報");
			lowerBox.add(new JLabel("患者番号"));
			lowerBox.add(Box.createHorizontalStrut(5));
			lowerBox.add(patientIdField);
			lowerBox.add(Box.createHorizontalStrut(5));
			lowerBox.add(registerButton);
			lowerBox.add(Box.createHorizontalStrut(5));
			lowerBox.add(patientInfoButton);
			lowerBox.add(Box.createHorizontalGlue());
		}
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(5));
		panel.add(lowerBox);
		add(panel, BorderLayout.NORTH);
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
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel upperBox = new JPanel();
		upperBox.setLayout(new BoxLayout(upperBox, BoxLayout.LINE_AXIS));
		JPanel lowerBox = new JPanel();
		lowerBox.setLayout(new BoxLayout(lowerBox, BoxLayout.LINE_AXIS));
		{
			JButton updateButton = new JButton("更新");
			JButton cashierButton = new JButton("会計");
			JButton unselectButton = new JButton("選択解除");
			JButton deleteButton = new JButton("削除");

			upperBox.add(updateButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(cashierButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(unselectButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(deleteButton);
			upperBox.add(Box.createHorizontalGlue());
		}
		{
			JButton closeButton = new JButton("終了");
			closeButton.addActionListener(event -> {
				dispose();
			});
			lowerBox.add(closeButton);
			lowerBox.add(Box.createHorizontalGlue());
		}
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(5));
		panel.add(lowerBox);
		add(panel, BorderLayout.SOUTH);
	}

}