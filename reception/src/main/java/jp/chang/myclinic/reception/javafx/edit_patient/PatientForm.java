package jp.chang.myclinic.reception.javafx.edit_patient;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

class PatientForm extends Form {

    //private static Logger logger = LoggerFactory.getLogger(PatientForm.class);
    private TextField lastNameInput = new TextField();
    {
        lastNameInput.setPromptText("姓");
    }
    private TextField firstNameInput = new TextField();
    {
        firstNameInput.setPromptText("名");
    }
    private TextField lastNameYomiInput = new TextField();
    {
        lastNameYomiInput.setPromptText("姓のよみ");
    }
    private TextField firstNameYomiInput = new TextField();
    {
        firstNameYomiInput.setPromptText("名のよみ");
    }
    private DateForm birthdayInput = new DateForm();
    {
        birthdayInput.setGengouList(Gengou.values());
        birthdayInput.setDateFormInputs(new DateFormInputs(Gengou.Shouwa, "", "", ""));
    }
    private RadioButtonGroup<Sex> sexInput = new RadioButtonGroup<>();
    private TextField addressInput = new TextField();
    private TextField phoneInput = new TextField();
    {
        phoneInput.getStyleClass().add("phone-input");
    }

    PatientForm() {
        getStyleClass().add("patient-form");
        RadioButton maleButton = sexInput.createRadioButton("男", Sex.Male);
        RadioButton femaleButton = sexInput.createRadioButton("女", Sex.Female);
        sexInput.setValue(Sex.Female);
        addWithHbox("名前", lastNameInput, firstNameInput);
        addWithHbox("よみ", lastNameYomiInput, firstNameYomiInput);
        add("生年月日", birthdayInput);
        addWithHbox("性別", maleButton, femaleButton);
        add("住所", addressInput);
        add("電話", phoneInput);
    }

    PatientFormInputs getInputs(){
        PatientFormInputs inputs = new PatientFormInputs();
        inputs.lastNameInput = lastNameInput.getText();
        inputs.firstNameInput = firstNameInput.getText();
        inputs.lastNameYomiInput = lastNameYomiInput.getText();
        inputs.firstNameYomiInput = firstNameYomiInput.getText();
        inputs.birthdayInputs = birthdayInput.getDateFormInputs();
        inputs.sexInput = sexInput.getValue();
        inputs.addressInput = addressInput.getText();
        inputs.phoneInput = phoneInput.getText();
        return inputs;
    }

}
