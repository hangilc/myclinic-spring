package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.consts.Sex;

class NewPatientDialog extends JDialog {
	NewPatientDialog(){
		setTitle("新規患者入力");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setupCenter();
		setupSouth();
		pack();
	}

	private JTextField lastNameField = new JTextField(6);
	private JTextField firstNameField = new JTextField(6);
	private JTextField lastNameYomiField = new JTextField(6);
	private JTextField firstNameYomiField = new JTextField(6);
	private JRadioButton maleButton = new JRadioButton(Sex.Male.getKanji());
	private JRadioButton femaleButton = new JRadioButton(Sex.Female.getKanji());
	private DateInput birthdayInput = new DateInput().setGengou("昭和");
	private JTextField addressField = new JTextField(30);
	private JTextField phoneField = new JTextField(15);
	private JButton okButton = new JButton("ＯＫ");
	private NewPatientHoken hokenPanel;

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
			c.gridx = 2;
			c.gridy = 2;
			panel.add(birthdayInput, c);		
		}
		{
			c.gridwidth = 2;
			c.gridx = 1;
			c.gridy = 3;
			panel.add(addressField, c);
			c.gridwidth = 1;
		}
		{
			c.gridwidth = 1;
			c.gridx = 2;
			c.gridy = 4;
			panel.add(phoneField, c);
		}
		{
			hokenPanel = new NewPatientHoken(this);
			c.gridwidth = 2;
			c.gridx = 1;
			c.gridy = 5;
			panel.add(hokenPanel, c);
		}
		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		okButton.addActionListener(event -> doEnter());
		JButton cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(event -> dispose());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(okButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cancelButton);
		panel.add(Box.createHorizontalGlue());
		add(panel, BorderLayout.SOUTH);
	}

	private Sex getSex(){
		if( maleButton.isSelected() ){
			return Sex.Male;
		} else if( femaleButton.isSelected() ){
			return Sex.Female;
		} else {
			return Sex.Female;
		}
	}

	private PatientDTO getPatientDTO(){
		PatientDTO patient = new PatientDTO();
		patient.lastName = lastNameField.getText();
		if( patient.lastName.isEmpty() ){
			JOptionPane.showMessageDialog(this, "姓が入力されていません。の入力が不適切です。");
			return null;
		}
		patient.firstName = firstNameField.getText();
		if( patient.firstName.isEmpty() ){
			JOptionPane.showMessageDialog(this, "名が入力されていません。の入力が不適切です。");
			return null;
		}
		patient.lastNameYomi = lastNameYomiField.getText();
		if( patient.lastNameYomi.isEmpty() ){
			JOptionPane.showMessageDialog(this, "姓のよみ方が入力されていません。の入力が不適切です。");
			return null;
		}
		patient.firstNameYomi = firstNameYomiField.getText();
		if( patient.firstNameYomi.isEmpty() ){
			JOptionPane.showMessageDialog(this, "名のよみ方が入力されていません。の入力が不適切です。");
			return null;
		}
		patient.sex = getSex().getCode();
		try{
			LocalDate birthday = birthdayInput.getValue();
			patient.birthday = DateTimeFormatter.ISO_LOCAL_DATE.format(birthday);
		} catch(DateInput.DateInputException ex){
			JOptionPane.showMessageDialog(this, "生年月日の入力が不適切です。\n" + ex.getMessage());
			return null;
		}
		patient.address = addressField.getText();
		patient.phone = phoneField.getText();
		return patient;		
	}

	private void doEnter(){
		okButton.setEnabled(false);
		PatientHokenDTO patientHokenDTO = new PatientHokenDTO();
		patientHokenDTO.patientDTO = getPatientDTO();
		if( patientHokenDTO.patientDTO == null ){
			okButton.setEnabled(true);
			return;
		}
		patientHokenDTO.hokenDTO = new HokenDTO();
		patientHokenDTO.hokenDTO.shahokokuhoDTO = hokenPanel.getShahokokuhoDTO();
		patientHokenDTO.hokenDTO.koukikoureiDTO = hokenPanel.getKoukikoureiDTO();
		patientHokenDTO.hokenDTO.roujinDTO = hokenPanel.getRoujinDTO();
		patientHokenDTO.hokenDTO.kouhi1DTO = hokenPanel.getKouhi1DTO();
		patientHokenDTO.hokenDTO.kouhi2DTO = hokenPanel.getKouhi2DTO();
		patientHokenDTO.hokenDTO.kouhi3DTO = hokenPanel.getKouhi3DTO();
		Service.api.enterPatientWithHoken(patientHokenDTO)
			.whenComplete((PatientHokenDTO result, Throwable t) -> {
				if( t != null ){
					t.printStackTrace();
					JOptionPane.showMessageDialog(NewPatientDialog.this, "エラー\n" + t);
					okButton.setEnabled(true);
					return;
				}
				dispose();
			});
	}
}

