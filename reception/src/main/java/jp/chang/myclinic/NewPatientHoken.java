package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class NewPatientHoken extends JPanel {

	private JList hokenList;
	private JDialog owner;

	NewPatientHoken(JDialog owner){
		this.owner = owner;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(makeUpperPanel());
		add(Box.createVerticalStrut(5));
		add(makeLowerPanel());
	}

	private JComponent makeUpperPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		hokenList = new JList();
		hokenList.setPreferredSize(new Dimension(400, 200));
		panel.add(hokenList);
		panel.add(Box.createHorizontalStrut(5));
		JPanel commandBox = new JPanel();
		{
			commandBox.setLayout(new BoxLayout(commandBox, BoxLayout.PAGE_AXIS));
			JButton editButton = new JButton("訂正");
			JButton deleteButton = new JButton("削除");
			commandBox.add(editButton);
			commandBox.add(Box.createVerticalStrut(5));
			commandBox.add(deleteButton);
		}
		panel.add(commandBox);
		return panel;
	}

	private JComponent makeLowerPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel upperBox = new JPanel();
		{
			upperBox.setLayout(new FlowLayout());
			JButton enterShahoButton = new JButton("新規社保国保");
			enterShahoButton.addActionListener(event -> {
				ShahoKokuhoForm form = new ShahoKokuhoForm(owner);
				form.setLocationByPlatform(true);
				form.setVisible(true);
			});
			upperBox.add(enterShahoButton);
			JButton enterKoukiButton = new JButton("新規後期高齢");
			JButton enterKouhiButton = new JButton("新規公費負担");
			upperBox.add(enterKoukiButton);
			upperBox.add(enterKouhiButton);
		}
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(5));
		JPanel lowerBox = new JPanel();
		{
			lowerBox.setLayout(new BoxLayout(lowerBox, BoxLayout.LINE_AXIS));
			JCheckBox onlyCurrentBox = new JCheckBox("現在有効のみ");
			onlyCurrentBox.setSelected(true);
			lowerBox.add(onlyCurrentBox);
		}
		panel.add(lowerBox);
		return panel;
	}

}