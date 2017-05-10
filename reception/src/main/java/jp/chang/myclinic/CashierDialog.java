package jp.chang.myclinic;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;

import java.awt.*;
import javax.swing.*;

class CashierDialog extends JDialog {

	private MeisaiDTO meisai;
	private PatientDTO patient;

	CashierDialog(JFrame owner, MeisaiDTO meisai, PatientDTO patient){
		super(owner, "会計", true);
		this.meisai = meisai;
		this.patient = patient;
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		{
			JLabel label = new JLabel(makeLabelContent());
			label.setPreferredSize(new Dimension(300, 500));
			panel.add(label);
		}
		panel.add(Box.createHorizontalStrut(5));
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
			JButton printReceiptButton = new JButton("領収書発行");
			box.add(printReceiptButton);
			box.add(Box.createVerticalStrut(5));
			JButton printBlankButton = new JButton("記入用領収書");
			box.add(printBlankButton);
			panel.add(box);
		}
		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("OK");
		panel.add(okButton);
		panel.add(Box.createHorizontalStrut(5));
		JButton cancelButton = new JButton("キャンセル");
		panel.add(cancelButton);
		add(panel, BorderLayout.SOUTH);
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