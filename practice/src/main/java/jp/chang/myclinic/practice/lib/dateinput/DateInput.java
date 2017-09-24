package jp.chang.myclinic.practice.lib.dateinput;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DateInput extends JPanel {

    private GengouInput gengouInput;
    private JTextField nenInput = new JTextField(2);
    private JTextField monthInput = new JTextField(2);
    private JTextField dayInput = new JTextField(2);
    private List<String> errors = new ArrayList<>();

    public DateInput(List<Gengou> gengouList){
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

    public DateInput(Gengou gengou){
        this(Collections.singletonList(gengou));
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
            return Optional.empty();
        }
    }

    public List<String> getErrors(){
        return errors;
    }

}
