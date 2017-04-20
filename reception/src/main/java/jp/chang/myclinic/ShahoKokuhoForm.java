package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class ShahoKokuhoForm extends JDialog {

	ShahoKokuhoForm(JDialog owner){
		super(owner, "新規社保国保入力", true);
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
		panel.add(new JLabel("保険者番号"), c);
		c.gridx = 1;
		c.gridy = 0;
		{
			JTextField patientIdField = new JTextField(6);
			panel.add(patientIdField, c);
		}
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("被保険者"), c);
		c.gridx = 1;
		c.gridy = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new FlowLayout());
			box.add(new JLabel("記号"));
			JTextField kigouField = new JTextField(10);
			box.add(kigouField);
			box.add(new JLabel("番号"));
			JTextField bangouField = new JTextField(10);
			box.add(bangouField);
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new JLabel("本人・家族"), c);
		c.gridx = 1;
		c.gridy = 2;
		{
			JPanel box = new JPanel();
			box.setLayout(new FlowLayout());
			JRadioButton honninButton = new JRadioButton("本人");
			JRadioButton kazokuButton = new JRadioButton("家族");
			kazokuButton.setSelected(true);
			ButtonGroup honninGroup = new ButtonGroup();
			honninGroup.add(honninButton);
			honninGroup.add(kazokuButton);
			box.add(honninButton);
			box.add(kazokuButton);
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new JLabel("資格取得日"), c);
		c.gridx = 1;
		c.gridy = 3;
		{
			DateInput validFromInput = new DateInput(new String[]{"平成"});
			panel.add(validFromInput, c);
		}
		c.gridx = 0;
		c.gridy = 4;
		panel.add(new JLabel("有効期限"), c);
		c.gridx = 1;
		c.gridy = 4;
		{
			DateInput validFromInput = new DateInput(new String[]{"平成"});
			panel.add(validFromInput, c);
		}
		c.gridx = 0;
		c.gridy = 5;
		panel.add(new JLabel("高齢"), c);
		c.gridx = 1;
		c.gridy = 5;
		{
			JPanel box = new JPanel();
			JRadioButton noKoureiButton = new JRadioButton("高齢でない");
			JRadioButton kourei1wariButton = new JRadioButton("１割");
			JRadioButton kourei2wariButton = new JRadioButton("２割");
			JRadioButton kourei3wariButton = new JRadioButton("３割");
			noKoureiButton.setSelected(true);
			ButtonGroup koureiGroup = new ButtonGroup();
			koureiGroup.add(noKoureiButton);
			koureiGroup.add(kourei1wariButton);
			koureiGroup.add(kourei2wariButton);
			koureiGroup.add(kourei3wariButton);
			box.add(noKoureiButton);
			box.add(kourei1wariButton);
			box.add(kourei2wariButton);
			box.add(kourei3wariButton);
			panel.add(box, c);
		}

		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton enterButton = new JButton("入力");
		JButton cancelButton = new JButton("キャンセル");
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(enterButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cancelButton);
		add(panel, BorderLayout.SOUTH);
	}
}