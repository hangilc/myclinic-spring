package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import jp.chang.myclinic.MyclinicConsts.WqueueState;
import jp.chang.myclinic.dto.*;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.io.IOException;


class MainFrame extends JFrame {

	private NewPatientDialog newPatientDialog;

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
			newPatientButton.addActionListener(this::doNewPatient);
			JButton searchPatientButton = new JButton("患者検索");
			searchPatientButton.addActionListener(event -> {
				SearchPatientDialog dialog = new SearchPatientDialog(this);
				dialog.setLocationByPlatform(true);
				dialog.setVisible(true);
			});
			JButton searchPaymentButton = new JButton("会計検索");
			searchPaymentButton.addActionListener(event -> {
				SearchPaymentDialog dialog = new SearchPaymentDialog(this);
				dialog.setLocationByPlatform(true);
				dialog.setVisible(true);
			});
			JButton receiptButton = new JButton("領収証用紙");


			upperBox.add(newPatientButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(searchPatientButton);
			upperBox.add(Box.createHorizontalStrut(5));
			upperBox.add(searchPaymentButton);
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
			updateButton.addActionListener(event -> doUpdate());
			JButton cashierButton = new JButton("会計");
			cashierButton.addActionListener(event -> {
				CashierDialog dialog = new CashierDialog(this);
				dialog.setLocationByPlatform(true);
				dialog.setVisible(true);
			});
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
				System.exit(0);
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

	private void doNewPatient(ActionEvent event){
		if( newPatientDialog == null ){
			newPatientDialog = new NewPatientDialog();
			newPatientDialog.setLocationByPlatform(true);
		}
		newPatientDialog.setVisible(true);
	}

	private void doUpdate(){
		try{
			List<WqueueFullDTO> list = Service.listWqueue();
			List<WqueueData> dataList = list.stream()
				.map(wq -> {
					return new WqueueData(wq.wqueue.waitState, "LABEL");
				})
				.collect(Collectors.toList());
		} catch(IOException ex){
			JOptionPane.showMessageDialog(this, ex.toString());
		}
	}

}