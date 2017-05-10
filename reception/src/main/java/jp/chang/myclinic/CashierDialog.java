package jp.chang.myclinic;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import javax.swing.*;

import static javax.swing.SwingConstants.TOP;

class CashierDialog extends JDialog {

	private MeisaiDTO meisai;
	private PatientDTO patient;

	CashierDialog(JFrame owner, MeisaiDTO meisai, PatientDTO patient){
		super(owner, "会計", true);
		this.meisai = meisai;
		this.patient = patient;
		setLayout(new MigLayout("fill", "[fill]", "[] []"));
		add(makeCenter(), "wrap");
		add(makeSouth());
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
		// *** (***) 様　患者番号 ***
		// お薬　*　種類
		// 請求金額 *** 円
		// 再診 *** 点
		// ...
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append(String.format("<div>%s %s (%s %s) 様 患者番号：%s</div>",
				patient.lastName, patient.firstName, patient.lastNameYomi, patient.firstNameYomi,
				patient.patientId));
		sb.append("</html>");
		return sb.toString();
	}

}