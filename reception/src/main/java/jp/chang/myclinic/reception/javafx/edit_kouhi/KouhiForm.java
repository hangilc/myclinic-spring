package jp.chang.myclinic.reception.javafx.edit_kouhi;

import javafx.scene.control.TextField;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.utilfx.dateinput.DateForm;

class KouhiForm extends Form {

    private TextField futanshaBangouInput = new TextField();
    private TextField jukyuushaBangouInput = new TextField();
    private DateForm validFromInput = new DateForm(Gengou.Recent, Gengou.Current);
    private DateForm validUptoInput = new DateForm(Gengou.Recent, Gengou.Current);

    KouhiForm() {
        getStyleClass().add("kouhi-form");
        add("負担者番号", futanshaBangouInput);
        add("受給者番号", jukyuushaBangouInput);
        add("資格取得日", validFromInput);
        add("有効期限", validUptoInput);
    }

    void setInputs(KouhiFormInputs inputs) {
        jukyuushaBangouInput.setText(inputs.jukyuushaBangou);
        futanshaBangouInput.setText(inputs.futanshaBangou);
        validFromInput.setDateFormInputs(inputs.validFromInputs);
        validUptoInput.setDateFormInputs(inputs.validUptoInputs);
    }

    KouhiFormInputs getInputs() {
        KouhiFormInputs inputs = new KouhiFormInputs();
        inputs.futanshaBangou = futanshaBangouInput.getText();
        inputs.jukyuushaBangou = jukyuushaBangouInput.getText();
        inputs.validFromInputs = validFromInput.getDateFormInputs();
        inputs.validUptoInputs = validUptoInput.getDateFormInputs();
        return inputs;
    }

}
