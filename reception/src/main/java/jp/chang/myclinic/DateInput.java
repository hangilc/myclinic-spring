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
	private JTextField nenField;
	private JTextField monthField;
	private JTextField dayField;

	private static final String[] defaultGengouList = new String[]{"明治", "大正", "昭和", "平成"};

	DateInput(){
		this(defaultGengouList);
	}

	DateInput(String[] gengouChoices){
		setLayout(new FlowLayout());
		gengouList = new JComboBox<String>(gengouChoices);
		gengouList.setPrototypeDisplayValue("平成 ");
		gengouList.setSelectedIndex(gengouChoices.length-1);
		nenField = new JTextField(3);
		monthField = new JTextField(3);
		dayField = new JTextField(3);
		add(gengouList);
		add(nenField);
		add(new JLabel("年"));
		add(monthField);
		add(new JLabel("月"));
		add(dayField);
		add(new JLabel("日"));
	}

	public DateInput setGengou(String gengou){
		gengouList.setSelectedItem(gengou);
		return this;
	}

	public boolean isEmpty(){
		return nenField.getText().isEmpty() && monthField.getText().isEmpty() &&
			dayField.getText().isEmpty();
	}

	public LocalDate getValue() throws DateInputException {
		String gengou = gengouList.getItemAt(gengouList.getSelectedIndex());
		JapaneseEra era = Gengou.fromKanji(gengou).getCode();
		int nen, month, day;
		try {
			nen = Integer.parseInt(nenField.getText());
		} catch(NumberFormatException ex){
			throw new DateInputException("年の値が適切でありません。");
		}
		try {
			month = Integer.parseInt(monthField.getText());
		} catch(NumberFormatException ex){
			throw new DateInputException("月の値が適切でありません。");
		}
		try {
			day = Integer.parseInt(dayField.getText());
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