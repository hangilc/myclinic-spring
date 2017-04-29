package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.chrono.JapaneseEra;
import java.time.chrono.JapaneseChronology;
import jp.chang.myclinic.consts.Gengou;

class DateInput extends JPanel {

	public class DateInputException extends Exception {
		DateInputException(String message){
			super(message);
		}
	}

	private JComboBox<String> gengouList;
	private JTextField birthdayNen;
	private JTextField birthdayMonth;
	private JTextField birthdayDay;

	private static final String[] defaultGengouList = new String[]{"明治", "大正", "昭和", "平成"};

	DateInput(){
		this(defaultGengouList);
	}

	DateInput(String[] gengouChoices){
		setLayout(new FlowLayout());
		gengouList = new JComboBox<String>(gengouChoices);
		gengouList.setPrototypeDisplayValue("平成 ");
		gengouList.setSelectedIndex(gengouChoices.length-1);
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

	public DateInput setGengou(String gengou){
		gengouList.setSelectedItem(gengou);
		return this;
	}

	public LocalDate getValue() throws DateInputException {
		String gengou = gengouList.getItemAt(gengouList.getSelectedIndex());
		JapaneseEra era = Gengou.fromKanji(gengou).getCode();
		int nen, month, day;
		try {
			nen = Integer.parseInt(birthdayNen.getText());
		} catch(NumberFormatException ex){
			throw new DateInputException("年の値が適切でありません。");
		}
		try {
			month = Integer.parseInt(birthdayMonth.getText());
		} catch(NumberFormatException ex){
			throw new DateInputException("月の値が適切でありません。");
		}
		try {
			day = Integer.parseInt(birthdayDay.getText());
		} catch(NumberFormatException ex){
			throw new DateInputException("日の値が適切でありません。");
		}
		int year = JapaneseChronology.INSTANCE.prolepticYear(era, nen);
		try {
			return LocalDate.of(year, month, day);
		} catch(DateTimeException ex){
			throw new DateInputException("入力した日付が不適切です。");
		}
	}
}