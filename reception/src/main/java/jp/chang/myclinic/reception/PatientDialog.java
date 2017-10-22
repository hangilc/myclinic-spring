package jp.chang.myclinic.reception;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.dto.*;

class PatientDialog extends JDialog {

	private JTextField lastNameField = new JTextField(6);
	private JTextField firstNameField = new JTextField(6);
	private JTextField lastNameYomiField = new JTextField(6);
	private JTextField firstNameYomiField = new JTextField(6);
	private JRadioButton maleButton = new JRadioButton(Sex.Male.getKanji());
	private JRadioButton femaleButton = new JRadioButton(Sex.Female.getKanji());
	private DateInput birthdayInput = new DateInput().setGengou("昭和");
	private JTextField addressField = new JTextField(30);
	private JTextField phoneField = new JTextField(15);
	private JButton enterPatientButton = new JButton("入力");
	private HokenEditor hokenEditor;
	private JButton closeButton = new JButton("閉じる");
	private PatientDTO currentPatient;

	PatientDialog(String title, PatientDTO patient, HokenListDTO hokenList){
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setupSexChoices();
		setupHokenEditor(patient, hokenList);
		setLayout(new MigLayout("fill, flowy", 
			"[grow, fill]", 
			"[] [grow, fill] []"));
		add(makePane1());
		add(makePane2());
		add(makePane3());
		if( patient != null ){
			setPatient(patient);
			PatientEditorRegistry.INSTANCE.register(patient.patientId, this);
		}
		bind();
		pack();
	}

	public void setPatient(PatientDTO patientDTO){
		currentPatient = patientDTO;
		lastNameField.setText(patientDTO.lastName);
		firstNameField.setText(patientDTO.firstName);
		lastNameYomiField.setText(patientDTO.lastNameYomi);
		firstNameYomiField.setText(patientDTO.firstNameYomi);
		setSex(patientDTO.sex);
		setBirthday(patientDTO.birthday);
		addressField.setText(patientDTO.address);
		phoneField.setText(patientDTO.phone);
		if( hokenEditor.getPatientId() == 0 ){
			hokenEditor.setPatientId(patientDTO.patientId);
		}
	}

	// public void setHokenList(HokenListDTO hokenListDTO){
	// 	hokenEditor.setHokenList(hokenListDTO);
	// }

	private void setupSexChoices(){
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(maleButton);
		buttonGroup.add(femaleButton);
		femaleButton.setSelected(true);
	}

	private void setupHokenEditor(PatientDTO patient, HokenListDTO hokenList){
		int patientId = patient != null ? patient.patientId : 0;
		hokenEditor = new HokenEditor(patientId, hokenList);
	}

	public int getKouhiListSize(){
		return hokenEditor.getKouhiListSize();
	}

	// public void setCurrentOnlySelected(boolean selected){
	// 	hokenEditor.setCurrentOnlySelected(selected);
	// }

	private JPanel makePane1(){
		JPanel panel = new JPanel(new MigLayout("insets 0, gapy 3", "[right] [grow]", ""));
		panel.setBorder(BorderFactory.createTitledBorder("基本情報"));
		panel.add(new JLabel("名前"), "");
		panel.add(lastNameField, "split 2");
		panel.add(firstNameField, "wrap");
		panel.add(new JLabel("よみ"), "");
		panel.add(lastNameYomiField, "split 2");
		panel.add(firstNameYomiField, "wrap");
		panel.add(new JLabel("生年月日"), "");
		panel.add(birthdayInput, "wrap");
		panel.add(new JLabel("性別"));
		panel.add(maleButton, "split 2");
		panel.add(femaleButton, "wrap");
		panel.add(new JLabel("住所"));
		panel.add(addressField, "grow, wrap");
		panel.add(new JLabel("電話番号"));
		panel.add(phoneField, "wrap");
		panel.add(enterPatientButton, "span 2, left, gaptop rel");
		return panel;
	}

	private JPanel makePane2(){
		JPanel panel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
		panel.setBorder(BorderFactory.createTitledBorder("保険情報"));
		panel.add(hokenEditor, "grow");
		return panel;
	}

	private JPanel makePane3(){
		JPanel panel = new JPanel(new MigLayout("right, insets 0", "", ""));
		panel.add(closeButton);
		return panel;
	}

	private void bind(){
		enterPatientButton.addActionListener(event -> doEnterPatient());
		closeButton.addActionListener(event -> doClose());
	}

	private void doEnterPatient(){
		PatientDTO patient = getPatientDTO();
		if( patient == null ){
			return;
		}
		PatientDialog self = this;
		Service.api.enterPatient(patient)
			.whenComplete((result, t) -> {
				if( t != null ){
					t.printStackTrace();
					alert("患者情報の入力に失敗しました。" + t);
					return;
				}
				int patientId = result;
				patient.patientId = patientId;
				if( currentPatient != null ){
					if( patientId != currentPatient.patientId ){
						throw new RuntimeException("cannot happen");
					}
					Broadcast.patientModified.broadcast(patient);
				} else {
					PatientEditorRegistry.INSTANCE.register(patientId, self);
				}
				alert("患者情報が入力されました。");
				EventQueue.invokeLater(() -> {
					setPatient(patient);
				});
			});
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

	protected void setEnterShahokokuhoButtonEnabled(boolean enabled){
		hokenEditor.setEnterShahokokuhoButtonEnabled(enabled);
	}

	protected void setEnterKoukikoureiButtonEnabled(boolean enabled){
		hokenEditor.setEnterKoukikoureiButtonEnabled(enabled);
	}

	protected void setEnterKouhiButtonEnabled(boolean enabled){
		hokenEditor.setEnterKouhiButtonEnabled(enabled);
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

	private void setSex(String sex){
		if( "M".equals(sex) ){
			maleButton.setSelected(true);
		} else {
			femaleButton.setSelected(true);
		}
	}

	private void setBirthday(String birthday){
		if( birthday == null || birthday.isEmpty() || "0000-00-00".equals(birthday) ){
			birthdayInput.clear();
		} else {
			LocalDate birthdayDate = LocalDate.parse(birthday, DateTimeUtil.sqlDateFormatter);
			birthdayInput.setValue(birthdayDate);
		}
	}

	private PatientDTO getPatientDTO(){
		PatientDTO patient = new PatientDTO();
		if( currentPatient != null ){
			patient.patientId = currentPatient.patientId;
		}
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

	private boolean isPatientInputEmpty(){
		return lastNameField.getText().isEmpty() &&
			firstNameField.getText().isEmpty() &&
			lastNameYomiField.getText().isEmpty() &&
			firstNameYomiField.getText().isEmpty() &&
			birthdayInput.isEmpty() &&
			addressField.getText().isEmpty() &&
			phoneField.getText().isEmpty();
	}

	private boolean isBirthdayInSync(){
		String birthday = currentPatient.birthday;
		if( birthday == null || birthday.isEmpty() || "0000-00-00".equals(birthday) ){
			return birthdayInput.isEmpty();
		} else {
			try {
				return LocalDate.parse(birthday, DateTimeUtil.sqlDateFormatter).equals(birthdayInput.getValue());
			} catch (DateInput.DateInputException ex){
				return false;
			}
		}
	}

	private boolean isPatientInputSync(){
		PatientDTO patient = currentPatient;
		return lastNameField.getText().equals(patient.lastName) &&
			firstNameField.getText().equals(patient.firstName) &&
			lastNameYomiField.getText().equals(patient.lastNameYomi) &&
			firstNameYomiField.getText().equals(patient.firstNameYomi) &&
			isBirthdayInSync() &&
			patient.sex.equals(getSex().getCode()) &&
			addressField.getText().equals(patient.address) &&
			phoneField.getText().equals(patient.phone);
	}

	private boolean isPatientInputDirty(){
		if( currentPatient == null ){
			return !isPatientInputEmpty();
		} else {
			return !isPatientInputSync();
		}
	}

	private void doClose(){
		if( isPatientInputDirty() ){
			int choice = JOptionPane.showConfirmDialog(this, "患者情報の入力が変更されていますが、このまま閉じますか？", "確認",
				JOptionPane.YES_NO_OPTION);
			if( choice != JOptionPane.YES_OPTION ){
				return;
			}
		}
		dispose();
	}

}