package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;

class ShahokokuhoForm extends Form {

    //private static Logger logger = LoggerFactory.getLogger(BaseShahokokuhoForm.class);
    TextField hokenshaBangouInput = new TextField();
    TextField hihokenshaKigouInput = new TextField();
    TextField hihokenshaBangouInput = new TextField();
    RadioButtonGroup<Integer> honninKazoku = new RadioButtonGroup<>();
    DateForm validFromInput = new DateForm();
    DateForm validUptoInput = new DateForm();
    RadioButtonGroup<Integer> kourei = new RadioButtonGroup<>();

    ShahokokuhoForm() {
        getStyleClass().add("shahokokuho-form");
        hokenshaBangouInput.getStyleClass().add("hokensha-bangou-input");
        add("保険者番号", hokenshaBangouInput);
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(
                    new Label("記号"),
                    hihokenshaKigouInput,
                    new Label("番号"),
                    hihokenshaBangouInput
            );
            add("被保険者", row);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_LEFT);
            RadioButton honninButton = honninKazoku.createRadioButton("本人", 1);
            RadioButton kazokuButton = honninKazoku.createRadioButton("家族", 0);
            row.getChildren().addAll(honninButton, kazokuButton);
            add("本人・家族", row);
        }
        add("交付年月日", validFromInput);
        add("有効期限", validUptoInput);
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_LEFT);
            RadioButton noKoureiButton = kourei.createRadioButton("高齢でない", 0);
            RadioButton futan1Button = kourei.createRadioButton("1割", 1);
            RadioButton futan2Button = kourei.createRadioButton("2割", 2);
            RadioButton futan3Button = kourei.createRadioButton("3割", 3);
            row.getChildren().addAll(noKoureiButton, futan1Button, futan2Button, futan3Button);
            add("高齢", row);
        }
    }

    public StringProperty hokenshaBangouProperty(){
        return hokenshaBangouInput.textProperty();
    }

    public StringProperty hihokenshaKigouProperty(){
        return hihokenshaKigouInput.textProperty();
    }

    public StringProperty hihokenshaBangouProperty(){
        return hihokenshaBangouInput.textProperty();
    }

    public IntegerProperty honninKazokuProperty(){
        return IntegerProperty.integerProperty(honninKazoku.valueProperty());
    }

    public DateForm validFromForm(){
        return validFromInput;
    }

    public DateForm validUptoForm(){
        return validUptoInput;
    }

    public IntegerProperty koureiProperty(){
        return IntegerProperty.integerProperty(kourei.valueProperty());
    }

}
