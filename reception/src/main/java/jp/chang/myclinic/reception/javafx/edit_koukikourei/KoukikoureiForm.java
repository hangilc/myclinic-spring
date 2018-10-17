package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;

class KoukikoureiForm extends Form {

    //private static Logger logger = LoggerFactory.getLogger(KoukikoureiForm.class);

    private TextField hokenshaBangouInput = new TextField();
    private TextField hihokenshaBangouInput = new TextField();
    private DateForm validFromInput = new DateForm();
    {
        validFromInput.setGengouList(Gengou.Recent);
    }
    private DateForm validUptoInput = new DateForm();
    {
        validUptoInput.setGengouList(Gengou.Recent);
    }
    private RadioButtonGroup<Integer> futanwari = new RadioButtonGroup<>();
    {
        futanwari.createRadioButton("１割", 1);
        futanwari.createRadioButton("２割", 2);
        futanwari.createRadioButton("３割", 3);
    }

    KoukikoureiForm() {
        getStyleClass().add("koukikourei-form");
        add("保険者番号", hokenshaBangouInput);
        add("被保険者番号", hihokenshaBangouInput);
        add("資格取得日", validFromInput);
        add("有効期限", validUptoInput);
        {
            HBox hbox = new HBox(4);
            hbox.getChildren().addAll(futanwari.getButtons());
            add("負担割", hbox);
        }
    }

    void setInputs(KoukikoureiFormInputs inputs){
        hokenshaBangouInput.setText(inputs.hokenshaBangou);
        hihokenshaBangouInput.setText(inputs.hihokenshaBangou);
        validFromInput.setDateFormInputs(inputs.validFromInputs);
        validUptoInput.setDateFormInputs(inputs.validUptoInputs);
        futanwari.setValue(inputs.futanwari);
    }

    KoukikoureiFormInputs getInputs(){
        KoukikoureiFormInputs inputs = new KoukikoureiFormInputs();
        inputs.hokenshaBangou = hokenshaBangouInput.getText();
        inputs.hihokenshaBangou = hihokenshaBangouInput.getText();
        inputs.validFromInputs = validFromInput.getDateFormInputs();
        inputs.validUptoInputs = validUptoInput.getDateFormInputs();
        inputs.futanwari = futanwari.getValue();
        return inputs;
    }

}
