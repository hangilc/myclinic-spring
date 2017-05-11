package jp.chang.myclinic;

import jp.chang.myclinic.dto.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class CashierDialog extends JDialog {

	private MeisaiDTO meisai;
	private PatientDTO patient;

	CashierDialog(JFrame owner, MeisaiDTO meisai, PatientDTO patient, ChargeDTO charge,
				  List<PaymentDTO> payments){
		super(owner, "会計", true);
		this.meisai = meisai;
		this.patient = patient;
		setLayout(new MigLayout("fill", "[fill]", "[] []"));
		add(makeCenter(), "wrap");
		add(makeSouth());
		pack();
		System.out.println(charge);
		System.out.println(payments);
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
			JButton printReceiptButton = new JButton("領収書発行");
			box.add(printReceiptButton, "sizegroup btn, wrap");
			JButton printBlankButton = new JButton("記入用領収書");
			box.add(printBlankButton, "sizegroup btn, gap push");
			panel.add(box, "top");
		}
		return panel;
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0, right", "", ""));
		JButton okButton = new JButton("OK");
		panel.add(okButton, "sizegroup btn");
		JButton cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(event -> dispose());
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
		sb.append(String.format("請求額 %d円", meisai.charge));
		sb.append("</div>");
		sb.append("</div>");
	}

}