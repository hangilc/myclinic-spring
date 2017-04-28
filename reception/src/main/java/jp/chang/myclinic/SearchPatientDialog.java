package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import jp.chang.myclinic.dto.*;
import java.util.List;
import java.io.IOException;

class SearchPatientDialog extends JDialog {

	private JTextField lastNameTextField = new JTextField(6);
	private JTextField firstNameTextField = new JTextField(6);
	private JTextField lastNameYomiTextField = new JTextField(6);
	private JTextField firstNameYomiTextField = new JTextField(6);

	SearchPatientDialog(JFrame owner){
		super(owner, "患者検索", true);
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(centerBox1());
		add(panel, BorderLayout.CENTER);
	}

	private JComponent centerBox1(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy = 0;
		panel.add(new JLabel("名前"), c);
		c.gridx = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameTextField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchByNameButton = new JButton("検索");
			searchByNameButton.addActionListener(this::onSearchByName);
			box.add(searchByNameButton);
			box.add(Box.createHorizontalGlue());
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("よみ"), c);
		c.gridx = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameYomiTextField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameYomiTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchByYomiButton = new JButton("検索");
			box.add(searchByYomiButton);
			box.add(Box.createHorizontalGlue());
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		{
			JButton recentButton = new JButton("最近の登録");
			panel.add(recentButton, c);
		}
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		{
			JList resultList = new JList();
			resultList.setPreferredSize(new Dimension(400, 300));
			panel.add(resultList, c);
		}
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		{
			JButton patientInfoButton = new JButton("患者情報");
			panel.add(patientInfoButton, c);
		}
		return panel;
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("閉じる");
		closeButton.addActionListener(event -> dispose());
		panel.add(closeButton);
		panel.add(Box.createHorizontalStrut(5));
		JButton registerButton = new JButton("診療受付");
		panel.add(registerButton);
		add(panel, BorderLayout.SOUTH);
	}

	private void onSearchByName(ActionEvent event){
		String lastName = lastNameTextField.getText();
		String firstName = firstNameTextField.getText();
		try{
			List<PatientDTO> result = Service.searchPatientByName(lastName, firstName);
			System.out.println(result);
		} catch(IOException ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "サーバーからデータを取得できませんでした。");
		}
	}

}