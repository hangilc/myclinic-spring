package jp.chang.myclinic.medicalcheck.lib;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SexRadioInput extends HBox {

    private static Logger logger = LoggerFactory.getLogger(SexRadioInput.class);
    RadioButtonGroup<Sex> group = new RadioButtonGroup<>();

    public SexRadioInput() {
        super(4);
        RadioButton maleButton = group.createRadioButton("男", Sex.Male);
        RadioButton femaleButton = group.createRadioButton("女", Sex.Female);
        getChildren().addAll(maleButton, femaleButton);
    }

    public SexRadioInput(Sex sex){
        this();
        setValue(sex);
    }

    public Sex getValue(){
        return group.getValue();
    }

    public void setValue(Sex sex){
        group.setValue(sex);
    }

}
