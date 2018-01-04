package jp.chang.myclinic.reception.javafx;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseEra;

public class DateInput extends HBox {

    private ChoiceBox<Gengou> gengouChoice = new ChoiceBox<>();
    private TextField nenInput = new TextField();
    private TextField monthInput = new TextField();
    private TextField dayInput = new TextField();
    private Property<LocalDate> value = new SimpleObjectProperty<>();
    public static LocalDate EMPTY_VALUE = LocalDate.MAX;

    public DateInput() {
        setupUI();
        value.bind(new ObjectBinding<LocalDate>(){
            {
                bind(gengouChoice.valueProperty(), nenInput.textProperty(),
                        monthInput.textProperty(), dayInput.textProperty());
            }
            @Override
            protected LocalDate computeValue() {
                return computeDate();
            }
        });
        value.addListener((obs, oldValue, newValue) -> {
            if( newValue == EMPTY_VALUE ){
                nenInput.setText("");
                monthInput.setText("");
                dayInput.setText("");
            } else if( newValue != null ){
                JapaneseEra era = DateTimeUtil.getEra(newValue);
                int nen = DateTimeUtil.getNen(newValue);
                Gengou gengou = Gengou.fromEra(era);
                gengouChoice.getSelectionModel().select(gengou);
                nenInput.setText("" + nen);
                monthInput.setText("" + newValue.getMonth().getValue());
                dayInput.setText("" + newValue.getDayOfMonth());
            }
        });
    }

    private void setupUI(){
        getChildren().addAll(gengouChoice, nenInput, new Label("年"), monthInput, new Label("月"),
                dayInput, new Label("日"));
        setAlignment(Pos.CENTER_LEFT);
        setMargin(nenInput, new Insets(0, 0, 0, 2));
        setMargin(monthInput, new Insets(0, 0, 0, 4));
        setMargin(dayInput, new Insets(0, 0, 0, 4));
        gengouChoice.setPrefWidth(64);
        gengouChoice.setMaxWidth(Control.USE_PREF_SIZE);
        gengouChoice.setMinWidth(Control.USE_PREF_SIZE);
        setTextFieldWidths(nenInput);
        setTextFieldWidths(monthInput);
        setTextFieldWidths(dayInput);
        gengouChoice.setConverter(new StringConverter<Gengou>(){
            @Override
            public String toString(Gengou gengou) {
                return gengou.getKanji();
            }

            @Override
            public Gengou fromString(String string) {
                return Gengou.fromKanji(string);
            }
        });
        setGengouItems(Gengou.Heisei, Gengou.Shouwa, Gengou.Taishou, Gengou.Meiji);
        selectGengou(Gengou.Shouwa);
    }

    private LocalDate computeDate(){
        Gengou gengou = gengouChoice.getSelectionModel().getSelectedItem();
        String nen = nenInput.getText();
        String month = monthInput.getText();
        String day = dayInput.getText();
        if( nen.isEmpty() && month.isEmpty() && day.isEmpty() ) {
            return EMPTY_VALUE;
        }
        try {
            JapaneseEra era = gengou.getEra();
            int nenValue = Integer.parseInt(nen);
            int monthValue = Integer.parseInt(month);
            int dayValue = Integer.parseInt(day);
            int yearValue = JapaneseChronology.INSTANCE.prolepticYear(era, nenValue);
            return LocalDate.of(yearValue, monthValue, dayValue);
        } catch(NumberFormatException ex){
            return null;
        }
    }

    private void setTextFieldWidths(TextField tf){
        tf.setPrefWidth(40);
        tf.setMaxWidth(Control.USE_PREF_SIZE);
        tf.setMinWidth(Control.USE_PREF_SIZE);
    }

    public void setGengouItems(Gengou... gengouItems){
        gengouChoice.getItems().clear();
        gengouChoice.getItems().addAll(gengouItems);
    }

    public void selectGengou(Gengou gengou){
        gengouChoice.getSelectionModel().select(gengou);
    }

    public LocalDate getValue(){
        return value.getValue();
    }

    public Property<LocalDate> valueProperty() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value.setValue(value);
    }

}
