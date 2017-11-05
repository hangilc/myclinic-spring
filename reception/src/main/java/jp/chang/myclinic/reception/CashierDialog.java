package jp.chang.myclinic.reception;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.preview.PreviewDialog;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawer;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.NumberUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.awt.Font.BOLD;

class CashierDialog extends JDialog {

	private MeisaiDTO meisai;
	private PatientDTO patient;
	private ChargeDTO charge;
	private List<PaymentDTO> payments;
	private int visitId;
	private ReceptionEnv receptionEnv;
	private JButton printReceiptButton;
	private JButton doneButton = new JButton("会計終了");
	private JButton cancelButton = new JButton("キャンセル");
	private boolean canceled = true;

	CashierDialog(JFrame owner, MeisaiDTO meisai, PatientDTO patient, ChargeDTO charge,
				  List<PaymentDTO> payments, int visitId, ReceptionEnv receptionEnv){
		super(owner, "会計", true);
		this.meisai = meisai;
		this.patient = patient;
		this.charge = charge;
		this.payments = payments;
		this.visitId = visitId;
		this.receptionEnv = receptionEnv;
		setLayout(new MigLayout("fill", "[fill]", "[] []"));
		add(makeCenter(), "wrap");
		add(makeSouth());
		bind();
		pack();
	}

	public boolean isCanceled(){
		return canceled;
	}

	private JComponent makeCenter(){
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0", "[]", "[]"));
		panel.add(makeMeisaiDetail(), "");
		{
			JPanel box = new JPanel(new MigLayout("insets 0 0 0 0", "", ""));
			printReceiptButton = new JButton("領収書発行");
			box.add(printReceiptButton, "sizegroup btn, wrap");
			JButton printBlankButton = new JButton("記入用領収書");
			box.add(printBlankButton, "sizegroup btn, gap push");
			panel.add(box, "top");
		}
		return panel;
	}

	private JComponent makeMeisaiDetail(){
		JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0px", "", ""));
		panel.add(new JLabel(patientLabel(patient)), "wrap");
		panel.add(new MeisaiDetailPane(meisai), "gaptop 5, wrap");
		panel.add(new JLabel("総点：" + NumberUtil.formatNumber(meisai.totalTen) + "点"), "gaptop 5, wrap");
		panel.add(new JLabel("負担割：" + meisai.futanWari + "割"), "wrap");
		panel.add(new JLabel("請求額：" + NumberUtil.formatNumber(charge.charge) + "円"), "wrap");
		if( payments.size() > 0 ){
			PaymentDTO payment = payments.get(0);
			panel.add(new JLabel("前回徴収額：" + NumberUtil.formatNumber(payment.amount) + "円"), "wrap");
		}
		panel.add(makeChargePane(), "gaptop 5");
		return panel;
	}

	private JComponent makeChargePane(){
		JPanel panel = new JPanel(new MigLayout("insets 3, gapy 0", "", ""));
		int diff = 0;
		if( payments.size() > 0 ){
			PaymentDTO payment = payments.get(0);
			diff = charge.charge - payment.amount;
		}
		panel.setBorder(BorderFactory.createLineBorder(diff >= 0 ? Color.BLACK : Color.RED, 2));
		Font curFont = getFont();
		Font font = curFont.deriveFont(BOLD, curFont.getSize() * 1.5f);
		String labelString = "請求額：" + NumberUtil.formatNumber(charge.charge) + "円";
		if( diff > 0 ){
			labelString += "（" + NumberUtil.formatNumber(diff) + "円 追加徴収）";
		} else if( diff < 0 ){
			labelString += "（" + NumberUtil.formatNumber(-diff) + "円 返金）";
		}
		JLabel label = new JLabel(labelString);
		label.setFont(font);
		panel.add(label);
		return panel;
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0, right", "", ""));
		panel.add(doneButton, "sizegroup btn");
		panel.add(cancelButton, "sizegroup btn");
		return panel;
	}

	private String patientLabel(PatientDTO patient){
		return String.format("(%d) %s %S ", patient.patientId, patient.lastName, patient.firstName);
	}

	private void bind(){
		printReceiptButton.addActionListener(event -> {
			final DataStore dataStore = new DataStore();
			Service.api.getClinicInfo()
					.thenCompose(clinicInfo -> {
						dataStore.clinicInfo = clinicInfo;
						return Service.api.getVisit(visitId);
					})
					.thenAccept(visit -> {
						ReceiptDrawerData data = ReceiptDrawerDataCreator.create(charge.charge, patient,
								visit, meisai, dataStore.clinicInfo);
						ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
						List<Op> ops = receiptDrawer.getOps();
						EventQueue.invokeLater(() -> {
//							ReceiptPreviewDialog dialog = new ReceiptPreviewDialog(this, ops);
							PrintManager printManager = new PrintManager(receptionEnv.getPrinterSettingsDir());
							PreviewDialog dialog = new PreviewDialog(this, "領収書プレビュー", printManager, receptionEnv.getPrinterSettingName())
									.setPageSize(148, 105);
							dialog.setPage(ops);
							dialog.pack();
							dialog.setLocationByPlatform(true);
							dialog.setVisible(true);
						});
					})
					.exceptionally(t -> {
						t.printStackTrace();
						EventQueue.invokeLater(() -> {
							alert(t.toString());
						});
						return null;
					});
		});
		// TODO: implement printBlankButton
		doneButton.addActionListener(event -> doDone());
		cancelButton.addActionListener(event -> doCancel());
	}

	private void doDone() {
		PaymentDTO payment = new PaymentDTO();
		payment.visitId = visitId;
		payment.amount = charge.charge;
		payment.paytime = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
		Service.api.finishCashier(payment)
				.thenAccept(result -> {
					EventQueue.invokeLater(() -> {
						canceled = false;
						dispose();
					});
				})
				.exceptionally(t -> {
					t.printStackTrace();
					EventQueue.invokeLater(() -> {
						alert(t.toString());
					});
					return null;
				});
	}

	private void doCancel(){
		canceled = true;
		dispose();
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

	private static class DataStore {
		ClinicInfoDTO clinicInfo;
	}
}