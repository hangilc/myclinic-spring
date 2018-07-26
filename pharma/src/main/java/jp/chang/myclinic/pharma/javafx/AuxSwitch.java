package jp.chang.myclinic.pharma.javafx;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AuxSwitch extends HBox {

    private static Logger logger = LoggerFactory.getLogger(AuxSwitch.class);
    private RadioButtonGroup<AuxMode> group = new RadioButtonGroup<>();
    private boolean reportChange = false;

    AuxSwitch() {
        super(4);
        getStyleClass().add("aux-switch");
        RadioButton byDateButton = group.createRadioButton("日にち順", AuxMode.ByDate);
        RadioButton byDrugButton = group.createRadioButton("薬剤別", AuxMode.ByDrug);
        group.setValue(AuxMode.ByDate);
        group.valueProperty().addListener((obs, oldValue, newValue) -> {
            if( reportChange ){
                onChange(newValue);
            }
        });
        getChildren().addAll(
                byDateButton,
                byDrugButton
        );
    }

    void reset(){
        reportChange = false;
        group.setValue(AuxMode.ByDate);
        reportChange = true;
    }

    void trigger(){
        onChange(group.getValue());
    }

    abstract void onChange(AuxMode mode);

}
