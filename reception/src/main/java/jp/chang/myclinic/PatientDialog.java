package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import jp.chang.myclinic.consts.Sex;

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
	//private NewPatientHoken hokenPanel;
	private HokenEditor hokenEditor = new HokenEditor();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("キャンセル");

	PatientDialog(String title){
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setupSexChoices();
		setLayout(new MigLayout("debug, fill, flowy", 
			"[grow, fill]", 
			"[] [grow, fill] []"));
		add(makePane1());
		add(makePane2());
		add(makePane3());
		pack();
	}

	private void setupSexChoices(){
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(maleButton);
		buttonGroup.add(femaleButton);
		femaleButton.setSelected(true);
	}

	private JPanel makePane1(){
		JPanel panel = new JPanel(new MigLayout("", "[right] [grow]", ""));
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
		JPanel panel = new JPanel(new MigLayout("fill", "", ""));
		hokenEditor.setBorder(BorderFactory.createEtchedBorder());
		panel.setBorder(BorderFactory.createTitledBorder("保険情報"));
		panel.add(hokenEditor, "");
		return panel;
	}

	private JPanel makePane3(){
		JPanel panel = new JPanel(new MigLayout("right", "", ""));
		panel.add(okButton, "sizegroup cmdbutton");
		panel.add(cancelButton, "sizegroup cmdbutton");
		return panel;
	}

}