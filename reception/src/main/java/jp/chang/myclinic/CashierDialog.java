package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawer;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

class CashierDialog extends JDialog {

	private MeisaiDTO meisai;
	private PatientDTO patient;
	private ChargeDTO charge;
	private List<PaymentDTO> payments;
	private int visitId;
	private JButton printReceiptButton;
	private JButton doneButton = new JButton("会計終了");
	private JButton cancelButton = new JButton("キャンセル");

	CashierDialog(JFrame owner, MeisaiDTO meisai, PatientDTO patient, ChargeDTO charge,
				  List<PaymentDTO> payments, int visitId){
		super(owner, "会計", true);
		this.meisai = meisai;
		this.patient = patient;
		this.charge = charge;
		this.payments = payments;
		this.visitId = visitId;
		setLayout(new MigLayout("fill", "[fill]", "[] []"));
		add(makeCenter(), "wrap");
		add(makeSouth());
		bind();
		pack();
	}

	private JComponent makeCenter(){
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0", "[grow]", "[grow]"));
		{
			JLabel label = new JLabel(makeLabelContent());
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setVerticalAlignment(SwingConstants.TOP);
			label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JScrollPane scroll = new JScrollPane(label);
			panel.add(scroll, "width :400:, height :300:, top, grow");
		}
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

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0, right", "", ""));
		panel.add(doneButton, "sizegroup btn");
		panel.add(cancelButton, "sizegroup btn");
		return panel;
	}

	private String makeLabelContent(){
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		addTopline(sb);
		addSections(sb);
		addSummary(sb);
		sb.append("</html>");
		return sb.toString();
	}

	private void addTopline(StringBuilder sb){
		sb.append(String.format("<div>%s %s (%s %s) 様 患者番号：%s</div>",
				patient.lastName, patient.firstName, patient.lastNameYomi, patient.firstNameYomi,
				patient.patientId));
	}

	private void addSections(StringBuilder sb){
		sb.append("<table>");
		for(MeisaiSectionDTO section: meisai.sections){
			addSection(sb, section);
		}
		sb.append("</table>");
	}

	private void addSection(StringBuilder sb, MeisaiSectionDTO section){
		sb.append("<tr>");
		sb.append("<td span='2'>");
		sb.append(String.format("[%s] (%d)", section.label, section.sectionTotalTen));
		sb.append("</td>");
		sb.append("</tr>");
		for(SectionItemDTO item: section.items){
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(item.label);
			sb.append("</td>");
			sb.append("<td align='right'>");
			sb.append(String.format("%dx%d=%d", item.tanka, item.count, item.tanka * item.count));
			sb.append("</td>");
			sb.append("</tr>");
		}
	}

	private void addSummary(StringBuilder sb){
		sb.append("<div>");
		sb.append("<div>");
		sb.append(String.format("総点 %d点", meisai.totalTen));
		sb.append("</div>");
		sb.append("<div>");
		sb.append(String.format("負担割 %d割", meisai.futanWari));
		sb.append("</div>");
		sb.append("<div>");
		if( payments.size() > 0 ){
			int chargeValue = charge.charge;
			int prevPay = payments.get(0).amount;
			int diff = chargeValue - prevPay;
			if( diff > 0 ){
				sb.append(String.format("追加請求額 %d円 （総請求額 %d円、以前の徴収額 %d円）", diff, charge.charge,  prevPay));
			}
		} else {
			sb.append(String.format("請求額 %d円", charge.charge));
		}
		if (meisai.charge != charge.charge) {
			sb.append(String.format("??? 請求額(%d)が計算値（%d）と異なります。", charge.charge, meisai.charge));
		}
		sb.append("</div>");
		sb.append("</div>");
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
							ReceiptPreviewDialog dialog = new ReceiptPreviewDialog(this, ops);
							dialog.setLocationByPlatform(true);
							dialog.setVisible(true);
						});

					})
					.exceptionally(t -> {
						t.printStackTrace();
						alert(t.toString());
						return null;
					});
		});
		doneButton.addActionListener(event -> doDone());
		cancelButton.addActionListener(event -> dispose());
	}

	private void doDone() {
		PaymentDTO payment = new PaymentDTO();
		payment.visitId = visitId;
		payment.amount = charge.charge;
		payment.paytime = DateTimeUtil.toSqlDateTime(LocalDateTime.now());
		Service.api.finishCashier(payment)
				.thenAccept(result -> {
					EventQueue.invokeLater(() -> {
						dispose();

					});
				})
				.exceptionally(t -> {
					t.printStackTrace();
					alert(t.toString());
					return null;
				});
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

	private static class DataStore {
		ClinicInfoDTO clinicInfo;
	}
}