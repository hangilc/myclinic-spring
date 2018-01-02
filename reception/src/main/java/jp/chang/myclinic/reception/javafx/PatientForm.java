package jp.chang.myclinic.reception.javafx;

import javafx.scene.control.Control;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientForm extends Form {

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
    private DateInput birthdayInput = new DateInput();
    private RadioButton maleButton = new RadioButton("男");
    private RadioButton femaleButton = new RadioButton("女");
    private ToggleGroup sexGroup = new ToggleGroup();
    {
        sexGroup.getToggles().addAll(maleButton, femaleButton);
        femaleButton.setSelected(true);
    }
    private TextField addressInput = new TextField();
    private TextField phoneInput = new TextField();
    {
        phoneInput.setPrefWidth(200);
        phoneInput.setMaxWidth(Control.USE_PREF_SIZE);
        phoneInput.setMinWidth(Control.USE_PREF_SIZE);
    }

    public PatientForm(){
        addWithHbox("名前", lastNameInput, firstNameInput);
        addWithHbox("よみ", lastNameYomiInput, firstNameYomiInput);
        add("生年月日", birthdayInput);
        addWithHbox("性別", maleButton, femaleButton);
        add("住所", addressInput);
        add("電話", phoneInput);
    }

    public List<String> save(PatientDTO patient){
        List<String> errs = new ArrayList<>();
        String lastName = lastNameInput.getText();
        if( lastName.isEmpty() ){
            errs.add("姓が入力されていません。");
        } else {
            patient.lastName = lastName;
        }
        String firstName = firstNameInput.getText();
        if( firstName.isEmpty() ){
            errs.add("名が入力されていません。");
        } else {
            patient.firstName = firstName;
        }
        String lastNameYomi = lastNameYomiInput.getText();
        if( lastNameYomi.isEmpty() ){
            errs.add("姓のよみが入力されていません。");
        } else {
            patient.lastNameYomi = lastNameYomi;
        }
        String firstNameYomi = firstNameYomiInput.getText();
        if( firstNameYomi.isEmpty() ){
            errs.add("名のよみが入力されていません。");
        } else {
            patient.firstNameYomi = firstNameYomi;
        }
        Result<LocalDate> birthdayResult = birthdayInput.getValue();
        if( birthdayResult.hasError() ){
            errs.add("生年月日の入力が不適切です。" + String.join("", birthdayResult.errors));
        } else {
            patient.birthday = birthdayResult.value.toString();
        }
        if( maleButton.isSelected() ){
            patient.sex = Sex.Male.getCode();
        } else if( femaleButton.isSelected() ){
            patient.sex = Sex.Female.getCode();
        } else {
            errs.add("Cannot happen in sex input");
        }
        patient.address = addressInput.getText();
        patient.phone = phoneInput.getText();
        return errs;
    }

}
