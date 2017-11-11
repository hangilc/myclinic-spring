package jp.chang.myclinic.reception;

import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.preview.PreviewDialog;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import jp.chang.myclinic.reception.receipt.ReceiptDrawer;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerDataCreator;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

class MainFrame extends JFrame {

	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private WqueueList wqueueList;
	private JButton newPatientButton = new JButton("新規患者");
	private JButton searchPatientButton = new JButton("患者検索");
	private JButton searchPaymentButton = new JButton("会計検索");
	private JButton printBlankReceiptButton = new JButton("領収証用紙");
	private JTextField patientIdField = new JTextField(6);
	private JButton registerButton = new JButton("診療受付");
	private JButton patientInfoButton = new JButton("患者情報");
	private JButton updateWqueueButton = new JButton("更新");
	private JButton cashierButton = new JButton("会計");
	private JButton unselectWqueueButton = new JButton("選択解除");
	private JButton deleteVisitButton = new JButton("削除");
	private JLabel messageBox = new JLabel("");
	private JButton closeButton = new JButton("終了");
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	MainFrame(){
		setTitle("受付");
		setLayout(new MigLayout("fill", "[grow]", ""));
		add(makeRow1(), "wrap");
		add(makeRow2(), "wrap");
		add(makeCenter(), "w 500, h 180, grow, wrap");
		add(makeRow3(), "wrap");
		add(makeSouth(), "grow, right");
		bind();
		pack();
		patientIdField.requestFocus();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent event){
				onClosing();
			}
		});
	}

	private JComponent makeRow1(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(newPatientButton);
		panel.add(searchPatientButton);
		panel.add(searchPaymentButton);
		panel.add(printBlankReceiptButton, "gapleft 30");
		return panel;
	}

	private JComponent makeRow2(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(new JLabel("患者番号"));
		panel.add(patientIdField);
		panel.add(registerButton);
		panel.add(patientInfoButton);
		return panel;
	}

	private JComponent makeCenter(){
		wqueueList = new WqueueList();
		wqueueList.setListData(new WqueueData[]{});
		JScrollPane sp = new JScrollPane(wqueueList);
		return sp;
	}

	private JComponent makeRow3(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(updateWqueueButton);
		panel.add(cashierButton);
		panel.add(unselectWqueueButton);
		panel.add(deleteVisitButton);
		return panel;
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", ""));
		JScrollPane sp = new JScrollPane(messageBox);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy((JScrollPane.VERTICAL_SCROLLBAR_NEVER));
		sp.setBorder(BorderFactory.createEmptyBorder());
		panel.add(sp, "grow");
		panel.add(closeButton);
		return panel;
	}

	private void bind(){
        newPatientButton.addActionListener(this::doNewPatient);
        searchPatientButton.addActionListener(event -> {
            SearchPatientDialog dialog = new SearchPatientDialog();
            dialog.setLocationByPlatform(true);
            dialog.setVisible(true);
        });
        searchPaymentButton.addActionListener(event -> {
            SearchPaymentDialog dialog = new SearchPaymentDialog(this);
            dialog.setLocationByPlatform(true);
            dialog.setVisible(true);
        });
        printBlankReceiptButton.addActionListener(event -> doPrintBlankReceipt());
        registerButton.addActionListener(event -> doRegister());
        patientInfoButton.addActionListener(event -> doPatientInfo());
        updateWqueueButton.addActionListener(event -> doUpdateWqueue());
        cashierButton.addActionListener(event -> doCashier());
        unselectWqueueButton.addActionListener(event -> {
        	wqueueList.setSelectedVisitId(0);
		});
        deleteVisitButton.addActionListener(event -> doDeleteVisit());
        closeButton.addActionListener(event -> onClosing());
	}

	private void doRegister() {
		try {
			int patientId = Integer.parseInt(patientIdField.getText());
			Service.api.getPatient(patientId)
					.thenCompose(patient -> {
						ConfirmRegisterDialog confirmRegisterDialog = new ConfirmRegisterDialog(this, patient);
						confirmRegisterDialog.setLocationByPlatform(true);
						confirmRegisterDialog.setVisible(true);
						if( confirmRegisterDialog.isCanceled() ){
							throw new CancellationException();
						} else {
							return Service.api.startVisit(patientId);
						}
					})
					.thenAccept(visitId -> {
						EventQueue.invokeLater(() -> {
							patientIdField.setText("");
							doUpdateWqueue();
						});
					})
					.exceptionally(t -> {
						if (!(isCancellation(t))) {
							t.printStackTrace();
							EventQueue.invokeLater(() -> {
								alert(t.toString());
							});
						}
						return null;
					});
		} catch (NumberFormatException e) {
			alert("患者番号の入力が不適切です。");
		}
	}

	private boolean isCancellation(Throwable t){
		if( t instanceof CompletionException){
			CompletionException ce = (CompletionException)t;
			if( ce.getCause() instanceof CancellationException ){
				return true;
			}
		}
		return false;
	}

	private void doPatientInfo() {
		try {
			int patientId = Integer.parseInt(patientIdField.getText());
			Service.api.getPatient(patientId)
					.thenAccept((PatientDTO patient) -> EventQueue.invokeLater(() -> {
						PatientInfoDialog patientInfoDialog = new PatientInfoDialog(this, patient, true);
						patientInfoDialog.setLocationByPlatform(true);
						patientInfoDialog.setVisible(true);
						patientIdField.setText("");
					}))
					.exceptionally(t -> {
						t.printStackTrace();
						EventQueue.invokeLater(() -> {
							alert(t.toString());
						});
						return null;
					});
		} catch (NumberFormatException e) {
			alert("患者番号の入力が不適切です。");
		}
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
		PatientDialog newPatientDialog = new PatientDialog("新規患者入力", null, null);
		newPatientDialog.setLocationByPlatform(true);
		newPatientDialog.setVisible(true);
	}

	private void doPrintBlankReceipt(){
		ReceiptDrawerDataCreator creator = new ReceiptDrawerDataCreator();
		creator.setClinicInfo(ReceptionEnv.INSTANCE.getClinicInfo());
		ReceiptDrawerData data = creator.getData();
		ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
		final List<Op> ops = receiptDrawer.getOps();
		EventQueue.invokeLater(() -> {
			PrintManager printManager = new PrintManager(ReceptionEnv.INSTANCE.getPrinterSettingsDir());
			String settingName = ReceptionEnv.INSTANCE.getPrinterSettingName();
			PreviewDialog dialog = new PreviewDialog(this, "領収書プレビュー", printManager, settingName)
					.setPageSize(148, 105);
			dialog.setCallback(new PreviewDialog.Callback() {
				@Override
				public void onRememberSetting(String settingName) {
					ReceptionConfig config = ReceptionEnv.INSTANCE.getConfig();
					config.setPrinterSettingName(settingName);
					try {
						ReceptionEnv.INSTANCE.saveConfig();
						alert("印刷設定(" + settingName + ")を記憶しました。");
					} catch (IOException e) {
						logger.error("Failed to save config.", e);
						alert("設定ファイルの保存に失敗しました。");
					}
				}
			});
			dialog.setPage(ops);
			dialog.pack();
			dialog.setLocationByPlatform(true);
			dialog.setVisible(true);
		});
	}

	void doUpdateWqueue(){
		WqueueData wqueueData = wqueueList.getSelectedValue();
		final int selectedVisitId = wqueueData == null ? 0 : wqueueData.getVisitId();
		Service.api.listWqueue()
			.whenComplete((result, t) -> {
				if( t != null ){
					t.printStackTrace();
					EventQueue.invokeLater(() -> {
						messageBox.setForeground(Color.RED);
						messageBox.setText("患者リスト更新に失敗しました。" + LocalDateTime.now().format(timeFormatter));
					});
					return;
				}
				WqueueData[] dataList = result.stream()
					.map(this::toWqueueData)
					.toArray(size -> new WqueueData[size]);
				EventQueue.invokeLater(() -> {
					wqueueList.setListData(dataList);
					wqueueList.setSelectedVisitId(selectedVisitId);
					messageBox.setForeground(Color.BLACK);
					messageBox.setText("患者リスト更新 " + LocalDateTime.now().format(timeFormatter));
				});
			});
	}

	private void doCashier(){
		WqueueData wq = wqueueList.getSelectedValue();
		if( wq == null || wq.getState() != WqueueWaitState.WaitCashier ){
			return;
		}
		int visitId = wq.getVisitId() ;
		CompletableFuture<MeisaiDTO> meisaiFetcher = Service.api.getVisitMeisai(visitId);
		CompletableFuture<ChargeDTO> chargeFetcher = Service.api.getCharge(visitId);
		CompletableFuture<List<PaymentDTO>> paymentsFetcher = Service.api.listPayment(visitId);
		CompletableFuture.allOf(meisaiFetcher, chargeFetcher, paymentsFetcher)
				.whenComplete((r, t) -> {
					if( t != null ){
						alert("明細情報の取得に失敗しました。" + t.toString());
						return;
					}
					MeisaiDTO meisai = meisaiFetcher.join();
					ChargeDTO charge = chargeFetcher.join();
					List<PaymentDTO> payments = paymentsFetcher.join();
					EventQueue.invokeLater(() -> {
						CashierDialog dialog = new CashierDialog(this, meisai, wq.getPatient(), charge, payments, visitId);
						dialog.setLocationByPlatform(true);
						dialog.setVisible(true);
						if( !dialog.isCanceled() ){
							doUpdateWqueue();
						}
					});
				});
	}

	private void doDeleteVisit(){
		WqueueData wq = wqueueList.getSelectedValue();
		if( wq == null ){
			return;
		}
//		if( wq.getState() != WqueueWaitState.WaitExam ){
//			alert("診察待ちでないので、削除できません。");
//			return;
//		}
		String confirmMessage = String.format("%s%s様（%d）の受付を削除していいですか？",
				wq.getPatient().lastName, wq.getPatient().firstName, wq.getPatient().patientId);
		if( !confirm(confirmMessage) ){
			return;
		}
		int visitId = wq.getVisitId() ;
		Service.api.deleteVisitFromReception(visitId)
				.thenAccept(r -> {
					EventQueue.invokeLater(() -> doUpdateWqueue());
				})
				.exceptionally(t -> {
					t.printStackTrace();
					EventQueue.invokeLater(() -> {
						alert(t.toString());
					});
					return null;
				});
	}

	private WqueueData toWqueueData(WqueueFullDTO wq){
		WqueueWaitState waitState = WqueueWaitState.fromCode(wq.wqueue.waitState);
		PatientDTO patient = wq.patient;
		String label = String.format("[%s] (%04d) %s %s (%s %s)", waitState.getLabel(),
			patient.patientId, patient.lastName, patient.firstName, patient.lastNameYomi, patient.firstNameYomi);
		int visitId = wq.visit.visitId;
		return new WqueueData(waitState, label, visitId, wq.patient);
	}

	private void alert(String message){
        JOptionPane.showMessageDialog(MainFrame.this, message);
    }

	private boolean confirm(String message){
		int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
		return choice == JOptionPane.YES_OPTION;
	}

}