package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class SearchPaymentDialog extends JDialog {

	SearchPaymentDialog(JFrame owner){
		super(owner, "会計検索", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		{
			JButton recentPaymentsButton = new JButton("最近の会計");
			panel.add(recentPaymentsButton);
		}
		panel.add(Box.createVerticalStrut(5));
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(new JLabel("患者番号"));
			box.add(Box.createHorizontalStrut(5));
			JTextField searchTextField = new JTextField(8);
			box.add(searchTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchButton = new JButton("検索");
			box.add(searchButton);
		}
		add(panel, BorderLayout.SOUTH);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("閉じる");
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

}