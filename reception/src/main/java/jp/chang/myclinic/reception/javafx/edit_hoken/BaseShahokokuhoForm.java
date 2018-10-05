package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.scene.control.TextField;
import jp.chang.myclinic.reception.javafx.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BaseShahokokuhoForm extends Form {

    private static Logger logger = LoggerFactory.getLogger(BaseShahokokuhoForm.class);
    TextField hokenshaBangouInput = new TextField();

    BaseShahokokuhoForm() {
        getStyleClass().add("shahokokuho-form");
        hokenshaBangouInput.getStyleClass().add("hokensha-bangou-input");
        add("保険者番号", hokenshaBangouInput);
    }

}
