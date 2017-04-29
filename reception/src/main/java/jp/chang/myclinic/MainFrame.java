package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MainFrame extends JFrame {

	private WqueueList wqueueList;

	MainFrame(){
		setTitle("受付");
		setupNorth();
		setupCenter();
		setupSouth();
		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				onClosing();
			}
		});
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
		wqueueList = new WqueueList();
		wqueueList.setPreferredSize(new Dimension(500, 300));
		wqueueList.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		add(wqueueList, BorderLayout.CENTER);
		wqueueList.setListData(new WqueueData[]{});
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
			closeButton.addActionListener(event -> onClosing());
			lowerBox.add(closeButton);
			lowerBox.add(Box.createHorizontalGlue());
		}
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(5));
		panel.add(lowerBox);
		add(panel, BorderLayout.SOUTH);
	}

	private void onClosing(){
		int openWindows = 0;
		for(Window win: Window.getWindows()){
			if( win.isShowing() ){
				openWindows += 1;
			}
		}
		if( openWindows > 1 ){
			int choice = JOptionPane.showConfirmDialog(this, "完了していない画面がありますが、このまま終了しますか？",
				"終了の確認", JOptionPane.YES_NO_OPTION);
			if( choice != JOptionPane.YES_OPTION ){
				return;
			}
		}
		dispose();
		System.exit(0);
	}

	private void doNewPatient(ActionEvent event){
		NewPatientDialog newPatientDialog = new NewPatientDialog();
		newPatientDialog.setLocationByPlatform(true);
		newPatientDialog.setVisible(true);
	}

	private void doUpdate(){
		Service.api.listWqueue()
			.whenComplete((result, t) -> {
				if( t != null ){
					t.printStackTrace();
					EventQueue.invokeLater(() -> {
						JOptionPane.showMessageDialog(MainFrame.this, "サーバーアクセスエラー\n" + t.toString());
					});
					return;
				}
				WqueueData[] dataList = result.stream()
					.map(this::toWqueueData)
					.toArray(size -> new WqueueData[size]);
				EventQueue.invokeLater(() -> {
					wqueueList.setListData(dataList);
				});
			});
	}

	private WqueueData toWqueueData(WqueueFullDTO wq){
		WqueueWaitState waitState = WqueueWaitState.fromCode(wq.wqueue.waitState);
		PatientDTO patient = wq.patient;
		String label = String.format("[%s] (%04d) %s %s (%s %s)", waitState.getLabel(),
			patient.patientId, patient.lastName, patient.firstName, patient.lastNameYomi, patient.firstNameYomi);
		return new WqueueData(waitState, label);
	}

}