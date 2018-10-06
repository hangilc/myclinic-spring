package jp.chang.myclinic.utilfx.dateinput;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Gengou;

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

}
