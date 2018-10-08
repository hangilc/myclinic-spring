package jp.chang.myclinic.utilfx.dateinput;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.util.logic.date.DateLogic;

import java.util.List;

public class DateForm extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(DateForm.class);
    private GengouInput gengouInput;
    private TextField nenField = new TextField();
    private Label nenLabel = new Label("年");
    private TextField monthField = new TextField();
    private Label monthLabel = new Label("月");
    private TextField dayField = new TextField();
    private Label dayLabel = new Label("日");

    public DateForm() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("date-input");
        this.gengouInput = new GengouInput(Gengou.values());
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

    public ObjectProperty<Gengou> gengouProperty(){
        return gengouInput.valueProperty();
    }

    public StringProperty nenProperty(){
        return nenField.textProperty();
    }

    public StringProperty monthProperty(){
        return monthField.textProperty();
    }

    public StringProperty dayProperty(){
        return dayField.textProperty();
    }

    public void bindDateLogic(DateLogic dateLogic){
        gengouInput.valueProperty().bindBidirectional(dateLogic.gengouProperty());
        nenProperty().bindBidirectional(dateLogic.nenProperty());
        monthProperty().bindBidirectional(dateLogic.monthProperty());
        dayProperty().bindBidirectional(dateLogic.dayProperty());
    }

}
