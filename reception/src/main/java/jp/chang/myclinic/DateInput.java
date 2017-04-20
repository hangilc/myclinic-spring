package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;

class DateInput extends JPanel {

	private JComboBox<String> gengouList;
	private JTextField birthdayNen;
	private JTextField birthdayMonth;
	private JTextField birthdayDay;

	DateInput(String[] gengouChoices){
		setLayout(new FlowLayout());
		//gengouList = new JComboBox<String>(new String[]{"明治", "大正", "昭和", "平成"});
		gengouList = new JComboBox<String>(gengouChoices);
		birthdayNen = new JTextField(3);
		birthdayMonth = new JTextField(3);
		birthdayDay = new JTextField(3);
		add(gengouList);
		add(birthdayNen);
		add(new JLabel("年"));
		add(birthdayMonth);
		add(new JLabel("月"));
		add(birthdayDay);
		add(new JLabel("日"));
	}

	public void setGengou(String gengou){
		gengouList.setSelectedItem(gengou);
	}
}