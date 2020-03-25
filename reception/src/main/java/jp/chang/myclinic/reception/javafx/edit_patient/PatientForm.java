package jp.chang.myclinic.reception.javafx.edit_patient;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

class PatientForm extends Form {

    private Label patientIdLabel = new Label();
    {
        patientIdLabel.setId("PatientIdLabel");
    }

    private TextField lastNameInput = new TextField();
    {
        lastNameInput.setPromptText("姓");
        lastNameInput.setId("LastNameInput");
    }
    private TextField firstNameInput = new TextField();
    {
        firstNameInput.setPromptText("名");
        firstNameInput.setId("FirstNameInput");
    }
    private TextField lastNameYomiInput = new TextField();
    {
        lastNameYomiInput.setPromptText("姓のよみ");
        lastNameYomiInput.setId("LastNameYomiInput");
    }
    private TextField firstNameYomiInput = new TextField();
    {
        firstNameYomiInput.setPromptText("名のよみ");
        firstNameYomiInput.setId("FirstNameYomiInput");
    }
    private DateForm birthdayInput = new DateForm();
    {
        birthdayInput.setGengouList(Gengou.values());
        birthdayInput.setDateFormInputs(new DateFormInputs(Gengou.Shouwa, "", "", ""));
    }
    private RadioButtonGroup<Sex> sexInput = new RadioButtonGroup<>();
    private TextField addressInput = new TextField();
    {
        addressInput.setId("AddressInput");
    }
    private TextField phoneInput = new TextField();
    {
        phoneInput.getStyleClass().add("phone-input");
        phoneInput.setId("PhoneInput");
    }

    PatientForm(boolean withPatientId){
        getStyleClass().add("patient-form");
        RadioButton maleButton = sexInput.createRadioButton("男", Sex.Male);
        RadioButton femaleButton = sexInput.createRadioButton("女", Sex.Female);
        sexInput.setValue(Sex.Female);
        if( withPatientId ){
            add("患者番号", patientIdLabel);
        }
        addWithHbox("名前", lastNameInput, firstNameInput);
        addWithHbox("よみ", lastNameYomiInput, firstNameYomiInput);
        add("生年月日", birthdayInput);
        addWithHbox("性別", maleButton, femaleButton);
        add("住所", addressInput);
        add("電話", phoneInput);

    }

    PatientForm() {
        this(false);
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

    void setInputs(PatientFormInputs inputs){
        if( inputs.patientId != null ){
            patientIdLabel.setText("" + inputs.patientId);
        }
        lastNameInput.setText(inputs.lastNameInput);
        firstNameInput.setText(inputs.firstNameInput);
        lastNameYomiInput.setText(inputs.lastNameYomiInput);
        firstNameYomiInput.setText(inputs.firstNameYomiInput);
        birthdayInput.setDateFormInputs(inputs.birthdayInputs);
        sexInput.setValue(inputs.sexInput);
        addressInput.setText(inputs.addressInput);
        phoneInput.setText(inputs.phoneInput);
    }

    DateForm getBirthdayInput(){
        return birthdayInput;
    }

    RadioButtonGroup<Sex> getSexInput(){
        return sexInput;
    }

}
