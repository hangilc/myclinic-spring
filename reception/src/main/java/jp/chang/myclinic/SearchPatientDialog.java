package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class SearchPatientDialog extends JDialog {

	SearchPatientDialog(JFrame owner){
		super(owner, "患者検索", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();

		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("閉じる");
		panel.add(closeButton);
		panel.add(Box.createHorizontalStrut(5));
		JButton registerButton = new JButton("診療受付");
		panel.add(registerButton);
		add(panel, BorderLayout.SOUTH);
	}

}