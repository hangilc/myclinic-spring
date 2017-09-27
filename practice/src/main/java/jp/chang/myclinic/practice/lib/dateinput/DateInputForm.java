package jp.chang.myclinic.practice.lib.dateinput;

import jp.chang.myclinic.consts.Gengou;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.temporal.ChronoField;

public class DateInputForm extends JPanel implements DateInput {

    private GengouInput gengouInput;
    private JTextField nenInput = new JTextField(2);
    private JTextField monthInput = new JTextField(2);
    private JTextField dayInput = new JTextField(2);
    private List<String> errors = new ArrayList<>();

    public DateInputForm(List<Gengou> gengouList){
        setLayout(new MigLayout("insets 0, gapx 1", "", ""));
        gengouInput = new GengouInput(gengouList);
        add(gengouInput, "w 50");
        add(nenInput);
        add(new JLabel("年"));
        add(monthInput);
        add(new JLabel("月"));
        add(dayInput);
        add(new JLabel("日"));
    }

    public DateInputForm(Gengou gengou){
        this(Collections.singletonList(gengou));
    }

    public void setValue(LocalDate value){
        JapaneseDate jd = JapaneseDate.from(value);
        gengouInput.setEra(jd.getEra());
        int nen = jd.get(ChronoField.YEAR_OF_ERA);
        int month = value.getMonthValue();
        int day = value.getDayOfMonth();
        nenInput.setText("" + nen);
        monthInput.setText("" + month);
        dayInput.setText("" + day);
    }

    public Optional<LocalDate> getValue(){
        errors.clear();
        JapaneseEra era = gengouInput.getEra();
        Integer nen = null;
        try {
            nen = Integer.parseInt(nenInput.getText());
        } catch(NumberFormatException ex){
            errors.add("年の入力が不適切です。");
        }
        Integer month = null;
        try {
            month = Integer.parseInt(monthInput.getText());
        } catch(NumberFormatException ex){
            errors.add("月の入力が不適切です。");
        }
        Integer day = null;
        try {
            day = Integer.parseInt(dayInput.getText());
        } catch(NumberFormatException ex){
            errors.add("日の入力が不適切です。");
        }
        if( errors.size() == 0 ){
            return Optional.of(LocalDate.ofEpochDay(JapaneseDate.of(era, nen, month, day).toEpochDay()));
        } else {
            alert(String.join("\n", errors));
            return Optional.empty();
        }
    }

    public boolean isEmpty(){
        return nenInput.getText().isEmpty() && monthInput.getText().isEmpty() && dayInput.getText().isEmpty();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
