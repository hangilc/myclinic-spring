package jp.chang.myclinic.practice.javafx.parts.dateinput;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Gengou;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DateInput extends HBox {

    private GengouInput gengouInput;
    private TextField nenField = new TextField();
    private Label nenLabel = new Label("年");
    private TextField monthField = new TextField();
    private Label monthLabel = new Label("月");
    private TextField dayField = new TextField();
    private Label dayLabel = new Label("日");

    public DateInput() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("date-input");
        gengouInput = new GengouInput();
        nenField.getStyleClass().add("nen");
        monthField.getStyleClass().add("month");
        dayField.getStyleClass().add("day");
        getChildren().addAll(
                gengouInput,
                nenField,
                nenLabel,
                monthField,
                monthLabel,
                dayField,
                dayLabel
        );
    }

    public void setGengou(Gengou gengou) {
        gengouInput.getSelectionModel().select(gengou);
    }

    public void clear(){
        nenField.clear();
        monthField.clear();
        dayField.clear();
    }

    public boolean isEmpty(){
        return nenField.getText().isEmpty() && monthField.getText().isEmpty() &&
                dayField.getText().isEmpty();
    }

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

    public void getValue(BiConsumer<LocalDate,List<String>> cb){
        List<String> err = new ArrayList<>();
        LocalDate value = null;
        if( !isEmpty() ){
            int nen = 0, month = 0, day = 0;
            try {
                nen = Integer.parseInt(nenField.getText());
            } catch(NumberFormatException ex){
                err.add("年の入力が不適切です。");
            }
            try {
                month = Integer.parseInt(monthField.getText());
            } catch(NumberFormatException ex){
                err.add("月の入力が不適切です。");
            }
            try {
                day = Integer.parseInt(dayField.getText());
            } catch(NumberFormatException ex){
                err.add("日の入力が不適切です。");
            }
            try {
                Gengou gengou = gengouInput.getGengou();
                if( gengou == null ){
                    err.add("元号が設定されていません。");
                } else {
                    try {
                        value = LocalDate.ofEpochDay(JapaneseDate.of(gengou.getEra(), nen, month, day).toEpochDay());
                    } catch(DateTimeException ex){
                        err.add("日付の入力が不適切です。");
                    }
                }
            }
        }
        cb.accept(value, err);
    }
}
