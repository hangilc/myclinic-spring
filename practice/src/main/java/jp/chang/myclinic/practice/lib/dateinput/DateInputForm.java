package jp.chang.myclinic.practice.lib.dateinput;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.lib.Result;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DateInputForm extends JPanel implements DateInput {

    private GengouInput gengouInput;
    private JTextField nenInput = new JTextField(2);
    private JTextField monthInput = new JTextField(2);
    private JTextField dayInput = new JTextField(2);
    private boolean allowEmpty;

    public DateInputForm(List<Gengou> gengouList){
        setLayout(new MigLayout("insets 0, gapx 1", "", ""));
        gengouInput = new GengouInput(gengouList);
        Link yearLink = new Link("年");
        yearLink.setCallback(evt -> doAdvance(evt, ChronoUnit.YEARS));
        Link monthLink = new Link("月");
        monthLink.setCallback(evt -> doAdvance(evt, ChronoUnit.MONTHS));
        Link dayLink = new Link("日");
        dayLink.setCallback(evt -> doAdvance(evt, ChronoUnit.DAYS));
        add(gengouInput, "w 50");
        add(nenInput);
        add(yearLink);
        add(monthInput);
        add(monthLink);
        add(dayInput);
        add(dayLink);
    }

    public DateInputForm(Gengou gengou){
        this(Collections.singletonList(gengou));
    }

    @Override
    public void setValue(LocalDate value){
        if( value == null ){
            nenInput.setText("");
            monthInput.setText("");
            dayInput.setText("");
        } else {
            JapaneseDate jd = JapaneseDate.from(value);
            gengouInput.setEra(jd.getEra());
            int nen = jd.get(ChronoField.YEAR_OF_ERA);
            int month = value.getMonthValue();
            int day = value.getDayOfMonth();
            nenInput.setText("" + nen);
            monthInput.setText("" + month);
            dayInput.setText("" + day);
        }
    }

    public void setAllowEmpty(boolean allowEmpty){
        this.allowEmpty = allowEmpty;
    }

    public Result<LocalDate, List<String>> getValue(){
        if( allowEmpty && isEmpty() ){
            return new Result<>(null, null);
        }
        List<String> err = new ArrayList<>();
        JapaneseEra era = gengouInput.getEra();
        Integer nen = null;
        try {
            nen = Integer.parseInt(nenInput.getText());
        } catch(NumberFormatException ex){
            err.add("年の入力が不適切です。");
        }
        Integer month = null;
        try {
            month = Integer.parseInt(monthInput.getText());
        } catch(NumberFormatException ex){
            err.add("月の入力が不適切です。");
        }
        Integer day = null;
        try {
            day = Integer.parseInt(dayInput.getText());
        } catch(NumberFormatException ex){
            err.add("日の入力が不適切です。");
        }
        if( nen != null && month != null && day != null ){
            try {
                return new Result<>(LocalDate.ofEpochDay(JapaneseDate.of(era, nen, month, day).toEpochDay()));
            } catch(DateTimeException ex){
                err.add("不適切な月日です。");
            }
        }
        return new Result<>(null, err);
    }

    public boolean isEmpty(){
        return nenInput.getText().isEmpty() && monthInput.getText().isEmpty() && dayInput.getText().isEmpty();
    }

    public void clear(){
        nenInput.setText("");
        monthInput.setText("");
        dayInput.setText("");
    }

    private void doAdvance(MouseEvent evt, TemporalUnit unit){
        getValue()
                .ifPresent(date -> {
                    int amount = evt.isShiftDown() ? -1 : 1;
                    LocalDate newDate = date.plus(amount, unit);
                    setValue(newDate);
                })
                .ifError(errs -> alert("日付の設定が不適切です。"));

    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
