package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class MainFrame extends JFrame {

	MainFrame(){
		setTitle("受付");
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		WqueuePanel wqPanel = new WqueuePanel();
		add(wqPanel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton closeButton = new JButton("終了");
		closeButton.addActionListener(event -> {
			dispose();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

}