package jp.chang.myclinic.utilfx.dateinput;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.util.kanjidate.Gengou;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DateForm extends HBox {

    private GengouInput gengouInput;
    private TextField nenField = new TextField();
    private Label nenLabel = new Label("年");
    private TextField monthField = new TextField();
    private Label monthLabel = new Label("月");
    private TextField dayField = new TextField();
    private Label dayLabel = new Label("日");

    public DateForm() {
        this(Gengou.values(), null);
    }

    public DateForm(List<Gengou> gengouList, Gengou initialGengou){
        this(gengouList.toArray(new Gengou[]{}), initialGengou);
    }

    public DateForm(Gengou[] gengouArray, Gengou initialGengou){
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("date-input");
        this.gengouInput = new GengouInput(gengouArray);
        if( initialGengou != null ){
            gengouInput.setValue(initialGengou);
        }
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

    public void setGengouList(List<Gengou> gengouList){
        gengouInput.setGengouList(gengouList);
    }

    public void setGengouList(Gengou... gengouList){
        setGengouList(Arrays.asList(gengouList));
    }

    public boolean isEmpty(){
        return nenField.getText().isEmpty() && monthField.getText().isEmpty() &&
                dayField.getText().isEmpty();
    }

    public void clear(){
        nenField.setText("");
        monthField.setText("");
        dayField.setText("");
    }

    public void clear(Gengou gengou){
        gengouInput.setValue(gengou);
        clear();
    }

    public DateFormInputs getDateFormInputs(){
        DateFormInputs inputs = new DateFormInputs(gengouInput.getValue());
        inputs.nen = nenField.getText();
        inputs.month = monthField.getText();
        inputs.day = dayField.getText();
        return inputs;
    }

    public void setDateFormInputs(DateFormInputs inputs){
        if( inputs == null ){
            clear();
        } else {
            gengouInput.setValue(inputs.gengou);
            nenField.setText(inputs.nen);
            monthField.setText(inputs.month);
            dayField.setText(inputs.day);
        }
    }

    public void setNenLabelClickHandler(Consumer<MouseEvent> handler){
        nenLabel.setOnMouseClicked(handler::accept);
    }

    public void setMonthLabelClickHandler(Consumer<MouseEvent> handler){
        monthLabel.setOnMouseClicked(handler::accept);
    }

    public void setDayLabelClickHandler(Consumer<MouseEvent> handler){
        dayLabel.setOnMouseClicked(handler::accept);
    }

}
