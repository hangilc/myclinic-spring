package jp.chang.myclinic.medicalcheck.dateinput;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Gengou;

import java.util.function.Consumer;

public class DateInput extends HBox implements DateInputInterface {

    private GengouInput gengouInput;
    private TextField nenField = new TextField();
    private Label nenLabel = new Label("年");
    private TextField monthField = new TextField();
    private Label monthLabel = new Label("月");
    private TextField dayField = new TextField();
    private Label dayLabel = new Label("日");
    private boolean allowNull = false;

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

    @Override
    public Gengou getGengou() {
        return gengouInput.getValue();
    }

    @Override
    public String getNen() {
        return nenField.getText();
    }

    @Override
    public String getMonth() {
        return monthField.getText();
    }

    @Override
    public String getDay() {
        return dayField.getText();
    }

    public void setGengou(Gengou gengou) {
        gengouInput.setValue(gengou);
    }

    @Override
    public void setNen(String nen) {
        nenField.setText(nen);
    }

    @Override
    public void setMonth(String month) {
        monthField.setText(month);
    }

    @Override
    public void setDay(String day) {
        dayField.setText(day);
    }

    @Override
    public boolean getAllowNull(){
        return allowNull;
    }

    public void setAllowNull(boolean value){
        allowNull = value;
    }

    public void clear(){
        nenField.clear();
        monthField.clear();
        dayField.clear();
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
