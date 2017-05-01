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
	private JButton okButton = new JButton("ＯＫ");

	PatientDialog(String title){
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("debug, fill", 
			"[grow, fill]", 
			"[] [grow] []"));
		add(makePane1(), "wrap");
		add(makePane2(), "wrap");
		add(makePane3(), "wrap");
		pack();
	}	

	private JPanel makePane1(){
		JPanel panel = new JPanel(new MigLayout("", "[] [grow]", ""));
		panel.add(new JLabel("名前"), "right");
		panel.add(lastNameField, "split 2");
		panel.add(firstNameField, "wrap");
		panel.add(new JLabel("よみ"), "right");
		panel.add(lastNameYomiField, "split 2");
		panel.add(firstNameYomiField, "wrap");
		panel.add(new JLabel("生年月日"), "right");
		panel.add(birthdayInput, "wrap");
		// panel.add(new JLabel("よみ"), c);
		// panel.add(new JLabel("生年月日"), c);
		// panel.add(new JLabel("性別"), c);
		// panel.add(new JLabel("住所"), c);
		// panel.add(new JLabel("電話番号"), c);

		return panel;
	}

	private JPanel makePane2(){
		JPanel panel = new JPanel();
		return panel;
	}

	private JPanel makePane3(){
		JPanel panel = new JPanel();
		return panel;
	}

}