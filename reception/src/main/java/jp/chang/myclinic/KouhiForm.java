package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class KouhiForm extends JDialog {

	KouhiForm(JDialog owner){
		super(owner, "新規公費負担入力", true);
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

		c.gridy = 0;
		c.gridx = 0;
		panel.add(new JLabel("負担者番号"), c);
		c.gridx = 1;
		{
			JTextField hokenshaBangouField = new JTextField(6);
			panel.add(hokenshaBangouField, c);
		}
		
		c.gridy = 1;
		c.gridx = 0;
		panel.add(new JLabel("受給者番号"), c);
		c.gridx = 1;
		{
			JTextField hihokenshaBangou = new JTextField(6);
			panel.add(hihokenshaBangou, c);
		}

		c.gridy = 2;
		c.gridx = 0;
		panel.add(new JLabel("資格取得日"), c);
		c.gridx = 1;
		{
			DateInput validFromInput = new DateInput(new String[]{"平成"});
			panel.add(validFromInput, c);
		}

		c.gridy = 3;
		c.gridx = 0;
		panel.add(new JLabel("有効期限"), c);
		c.gridx = 1;
		{
			DateInput validUptoInput = new DateInput(new String[]{"平成"});
			panel.add(validUptoInput, c);
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