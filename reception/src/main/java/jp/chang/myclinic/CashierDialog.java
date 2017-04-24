package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class CashierDialog extends JDialog {

	CashierDialog(JFrame owner){
		super(owner, "会計", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		{
			JLabel label = new JLabel();
			// *** (***) 様　患者番号 ***
			// お薬　*　種類
			// 請求金額 *** 円
			// 再診 *** 点
			// ...
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

}