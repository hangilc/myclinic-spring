package jp.chang.myclinic.pharma.javafx;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.pharma.javafx.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AuxSwitch extends HBox {

    private static Logger logger = LoggerFactory.getLogger(AuxSwitch.class);
    private RadioButtonGroup<AuxMode> group = new RadioButtonGroup<>();

    AuxSwitch() {
        super(4);
        getStyleClass().add("aux-switch");
        RadioButton byDateButton = group.createRadioButton("日にち順", AuxMode.ByDate);
        RadioButton byDrugButton = group.createRadioButton("薬剤別", AuxMode.ByDrug);
        group.setValue(AuxMode.ByDate);
        getChildren().addAll(
                byDateButton,
                byDrugButton
        );
    }

    AuxMode getValue(){
        return group.getValue();
    }

    void setValue(AuxMode mode){
        group.setValue(mode);
    }

}
