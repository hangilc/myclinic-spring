package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class NewPatientDialog extends JDialog {
	NewPatientDialog(){
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("名前"), c);
		c.gridy = 1;
		panel.add(new JLabel("よみ"), c);
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(new JLabel("生年月日"), c);
		c.gridwidth = 1;
		c.gridy = 3;
		panel.add(new JLabel("住所"), c);
		c.gridy = 4;
		c.gridwidth = 2;
		panel.add(new JLabel("電話番号"), c);
		c.gridwidth = 1;
		c.gridy = 5;
		panel.add(new JLabel("保険"), c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 1;
		{
			JTextField lastNameField = new JTextField(6);
			JTextField firstNameField = new JTextField(6);
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameField);
			c.gridy = 0;
			c.gridwidth = 2;
			panel.add(box, c);
			c.gridwidth = 1;
		}
		{
			JTextField lastNameYomiField = new JTextField(6);
			JTextField firstNameYomiField = new JTextField(6);
			JRadioButton maleButton = new JRadioButton("男");
			JRadioButton femaleButton = new JRadioButton("女");
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(maleButton);
			buttonGroup.add(femaleButton);
			femaleButton.setSelected(true);
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameYomiField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameYomiField);
			box.add(Box.createHorizontalStrut(15));
			box.add(maleButton);
			box.add(femaleButton);
			c.gridy = 1;
			c.gridwidth = 2;
			panel.add(box, c);
			c.gridwidth = 1;
		}
		{
			JComboBox<String> gengouList = new JComboBox<String>(new String[]{"明治", "大正", "昭和", "平成"});
			gengouList.setSelectedIndex(2);
			JTextField birthdayNen = new JTextField(3);
			JTextField birthdayMonth = new JTextField(3);
			JTextField birthdayDay = new JTextField(3);
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(gengouList);
			box.add(Box.createHorizontalStrut(5));
			box.add(birthdayNen);
			box.add(Box.createHorizontalStrut(5));
			box.add(new JLabel("年"));
			box.add(Box.createHorizontalStrut(5));
			box.add(birthdayMonth);
			box.add(Box.createHorizontalStrut(5));
			box.add(new JLabel("月"));
			box.add(Box.createHorizontalStrut(5));
			box.add(birthdayDay);
			box.add(Box.createHorizontalStrut(5));
			box.add(new JLabel("日"));
			c.gridx = 2;
			c.gridy = 2;
			panel.add(box, c);		
		}
		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton okButton = new JButton("ＯＫ");
		JButton cancelButton = new JButton("キャンセル");
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(okButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cancelButton);
		panel.add(Box.createHorizontalGlue());
		add(panel, BorderLayout.SOUTH);
	}
}