package jp.chang.myclinic;

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
	private HokenEditor hokenEditor;
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("キャンセル");
	private int patientId;

	PatientDialog(String title){
		this(title, false);
	}

	PatientDialog(String title, boolean showCurrentOnly){
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setupSexChoices();
		setupHokenEditor(showCurrentOnly);
		setLayout(new MigLayout("fill, flowy", 
			"[grow, fill]", 
			"[] [grow, fill] []"));
		add(makePane1());
		add(makePane2());
		add(makePane3());
		bind();
		pack();
	}

	public void setPatient(PatientDTO patientDTO){
		patientId = patientDTO.patientId;
		lastNameField.setText(patientDTO.lastName);
		firstNameField.setText(patientDTO.firstName);
		lastNameYomiField.setText(patientDTO.lastNameYomi);
		firstNameYomiField.setText(patientDTO.firstNameYomi);
		setSex(patientDTO.sex);
		setBirthday(patientDTO.birthday);
		addressField.setText(patientDTO.address);
		phoneField.setText(patientDTO.phone);
	}

	public void setHokenList(HokenListDTO hokenListDTO){
		hokenEditor.setHokenList(hokenListDTO);
	}

	public void setShahokokuhoListener(HokenListener<ShahokokuhoDTO> shahokokuhoListener){
		hokenEditor.setShahokokuhoListener(shahokokuhoListener);
	}

	public void setKoukikoureiListener(HokenListener<KoukikoureiDTO> koukikoureiListener){
		hokenEditor.setKoukikoureiListener(koukikoureiListener);
	}

	public void setKouhiListener(HokenListener<KouhiDTO> kouhiListener){
		hokenEditor.setKouhiListener(kouhiListener);
	}

	private void setupSexChoices(){
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(maleButton);
		buttonGroup.add(femaleButton);
		femaleButton.setSelected(true);
	}

	private void setupHokenEditor(boolean showCurrentOnly){
		hokenEditor = new HokenEditor(showCurrentOnly);
	}

	public int getKouhiListSize(){
		return hokenEditor.getKouhiListSize();
	}

	public void setCurrentOnlySelected(boolean selected){
		hokenEditor.setCurrentOnlySelected(selected);
	}

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
		panel.add(phoneField, "");
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
		panel.add(okButton, "sizegroup cmdbutton");
		panel.add(cancelButton, "sizegroup cmdbutton");
		return panel;
	}

	private void bind(){
		okButton.addActionListener(event -> {
			okButton.setEnabled(false);
			PatientHokenListDTO patientHokenListDTO = new PatientHokenListDTO();
			patientHokenListDTO.patientDTO = getPatientDTO();
			if( patientHokenListDTO.patientDTO != null ){
				patientHokenListDTO.hokenListDTO = hokenEditor.getHokenListDTO();
				onEnter(patientHokenListDTO);
			} else {
				okButton.setEnabled(true);
			}
		});
		cancelButton.addActionListener(event -> onCancel());
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

	protected void setEnterButtonEnabled(boolean enabled){
		okButton.setEnabled(enabled);
	}

	protected void onCancel(){
		dispose();
	}

	protected void onEnter(PatientHokenListDTO patientDTO){

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
		patient.patientId = patientId;
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

}