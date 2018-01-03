package jp.chang.myclinic.reception.javafx;

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
import jp.chang.myclinic.reception.lib.DateUtil;
import jp.chang.myclinic.reception.lib.Result;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.chrono.JapaneseEra;

public class DateInput extends HBox {

    private ChoiceBox<Gengou> gengouChoice = new ChoiceBox<>();
    private TextField nenInput = new TextField();
    private TextField monthInput = new TextField();
    private TextField dayInput = new TextField();
    private Property<LocalDate> value = new SimpleObjectProperty<>();

    public DateInput() {
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
        initialize();
        gengouChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> updateValue());
        nenInput.textProperty().addListener((obs, oldValue, newValue) -> updateValue());
        monthInput.textProperty().addListener((obs, oldValue, newValue) -> updateValue());
        dayInput.textProperty().addListener((obs, oldValue, newValue) -> updateValue());
        value.addListener((observable, oldValue, newValue) -> {
            System.out.println("dateinput value: " + newValue);
            if( newValue != null ){
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

    private void setTextFieldWidths(TextField tf){
        tf.setPrefWidth(40);
        tf.setMaxWidth(Control.USE_PREF_SIZE);
        tf.setMinWidth(Control.USE_PREF_SIZE);
    }

    private void initialize(){
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

    public void setGengouItems(Gengou... gengouItems){
        gengouChoice.getItems().clear();
        gengouChoice.getItems().addAll(gengouItems);
    }

    public void selectGengou(Gengou gengou){
        gengouChoice.getSelectionModel().select(gengou);
    }

    private void updateValue(){
        Result<LocalDate> result = getResultValue();
        if( !result.hasError() ){
            value.setValue(result.value);
        } else {
            value.setValue(null);
        }
    }

    public Result<LocalDate> getResultValue(){
        return DateUtil.convertToLocalDate(gengouChoice.getValue(), nenInput.getText(), monthInput.getText(),
                dayInput.getText());
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

    public boolean isEmpty(){
        return nenInput.getText().isEmpty() && monthInput.getText().isEmpty() && dayInput.getText().isEmpty();
    }
}
