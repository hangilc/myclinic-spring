package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.chrono.JapaneseDate;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

class PatientInfo extends JPanel {

	private JTextArea nameLabel = new JTextArea();
	private JTextArea yomiLabel = new JTextArea();
	private JLabel birthdayLabel = new JLabel("");
	private JLabel sexLabel = new JLabel("");
	private JTextArea addressArea = new JTextArea();
	private JTextArea phoneLabel = new JTextArea();

	PatientInfo(){
		setLayout(new MigLayout("insets 0, fill", "[right] [grow]", ""));
		prepTextArea(nameLabel);
		prepTextArea(yomiLabel);
		prepTextArea(addressArea);
		prepTextArea(phoneLabel);
		add(new JLabel("名前："));
		add(nameLabel, "grow, wmin 10, wrap");
		add(new JLabel("よみ："));
		add(yomiLabel, "grow, wmin 10, wrap");
		add(new JLabel("生年月日："));
		add(birthdayLabel, "wrap");
		add(new JLabel("性別："));
		add(sexLabel, "wrap");
		add(new JLabel("住所："));
		add(addressArea, "grow, wmin 10, wrap");
		add(new JLabel("電話："));
		add(phoneLabel, "grow, wmin 10, wrap");
	}

	private void prepTextArea(JTextArea ta){
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setBackground(getBackground());
	}

	public void setPatient(PatientDTO patient){
		nameLabel.setText(patient.lastName + " " + patient.firstName);
		yomiLabel.setText(patient.lastNameYomi + " " + patient.firstNameYomi);
		setBirthday(patient.birthday);
		setSex(patient.sex);
		addressArea.setText(patient.address);
		phoneLabel.setText(patient.phone);
	}

	private void setBirthday(String str){
		if( str == null || str.isEmpty() || "0000-00-00".equals(str) ){
			birthdayLabel.setText("");
		} else {
			LocalDate birthday = LocalDate.parse(str, DateTimeUtil.sqlDateFormatter);
			birthdayLabel.setText(DateTimeUtil.toKanji(birthday, DateTimeUtil.kanjiFormatter1));
		}
	}

	private void setSex(String sex){
		this.sexLabel.setText("M".equals(sex) ? "男性" : "女性");
	}

}