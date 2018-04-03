package jp.chang.myclinic.medicalcheck;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.medicalcheck.dateinput.DateInput;
import jp.chang.myclinic.medicalcheck.lib.GuiUtil;
import jp.chang.myclinic.medicalcheck.lib.SexRadioInput;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Form extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(Form.class);
    private TextField nameField = new TextField();
    private DateInput birthdayInput = new DateInput();
    private SexRadioInput sexInput = new SexRadioInput(Sex.Female);
    private TextField addressField = new TextField();
    private TextField heightField = new TextField();
    private TextField weightField = new TextField();

    Form() {
        birthdayInput.setGengou(Gengou.Heisei);
        heightField.getStyleClass().add("height-input");
        weightField.getStyleClass().add("weight-input");
        addRow("氏名", nameField);
        addRow("生年月日", birthdayInput);
        addRow("性別", sexInput);
        addRow("住所", addressField);
        addRow("身長", heightField, new Label("cm"));
        addRow("体重", weightField, new Label("kg"));
    }

    void applyTo(Data data){
        data.name = nameField.getText();
        birthdayInput.getValue()
                .ifPresent(date -> {
                    data.birthday = DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
                })
                .ifError(errs -> {
                    GuiUtil.alertError(String.join("\n", errs));
                });
        data.sex = sexInput.getValue().getKanji();
        data.address = addressField.getText();
        data.height = getHeightValue();
        data.weight = getWeightValue();
    }

    private String getHeightValue(){
        String height = heightField.getText();
        if( height.isEmpty() ){
            height = "     ";
        }
        return height + " cm";
    }

    private String getWeightValue(){
        String weight = weightField.getText();
        if( weight.isEmpty() ){
            weight = "     ";
        }
        return weight + " kg";
    }

}
