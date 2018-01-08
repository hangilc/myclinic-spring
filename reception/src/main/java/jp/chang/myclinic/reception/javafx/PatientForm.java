package jp.chang.myclinic.reception.javafx;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.DateUtil;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientForm extends Form {

    private int patientId;
    private TextField lastNameInput = new TextField();
    private TextField firstNameInput = new TextField();
    private TextField lastNameYomiInput = new TextField();
    private TextField firstNameYomiInput = new TextField();
    private DateInput birthdayInput = new DateInput();
    private RadioButtonGroup<Sex> sexInput = new RadioButtonGroup<>();
    private TextField addressInput = new TextField();
    private TextField phoneInput = new TextField();

    public PatientForm(PatientDTO patient) {
        lastNameInput.setPromptText("姓");
        firstNameInput.setPromptText("名");
        lastNameYomiInput.setPromptText("姓のよみ");
        firstNameYomiInput.setPromptText("名のよみ");
        RadioButton maleButton = sexInput.createRadioButton("男", Sex.Male);
        RadioButton femaleButton = sexInput.createRadioButton("女", Sex.Female);
        sexInput.setValue(Sex.Female);
        phoneInput.setPrefWidth(200);
        if (patient != null && patient.patientId > 0) {
            add("患者番号", new Label("" + patient.patientId));
        }
        addWithHbox("名前", lastNameInput, firstNameInput);
        addWithHbox("よみ", lastNameYomiInput, firstNameYomiInput);
        add("生年月日", birthdayInput);
        addWithHbox("性別", maleButton, femaleButton);
        add("住所", addressInput);
        add("電話", phoneInput);
        if (patient != null) {
            patientId = patient.patientId;
            lastNameInput.setText(patient.lastName);
            firstNameInput.setText(patient.firstName);
            lastNameYomiInput.setText(patient.lastNameYomi);
            firstNameYomiInput.setText(patient.firstNameYomi);
            birthdayInput.setValue(DateUtil.sqlDateToLocalDate(patient.birthday));
            sexInput.setValue(Sex.fromCode(patient.sex));
            addressInput.setText(patient.address);
            phoneInput.setText(patient.phone);
        }
    }

    public List<String> save(PatientDTO patient) {
        patient.patientId = patientId;
        List<String> errs = new ArrayList<>();
        String lastName = lastNameInput.getText();
        if (lastName.isEmpty()) {
            errs.add("姓が入力されていません。");
        } else {
            patient.lastName = lastName;
        }
        String firstName = firstNameInput.getText();
        if (firstName.isEmpty()) {
            errs.add("名が入力されていません。");
        } else {
            patient.firstName = firstName;
        }
        String lastNameYomi = lastNameYomiInput.getText();
        if (lastNameYomi.isEmpty()) {
            errs.add("姓のよみが入力されていません。");
        } else {
            patient.lastNameYomi = lastNameYomi;
        }
        String firstNameYomi = firstNameYomiInput.getText();
        if (firstNameYomi.isEmpty()) {
            errs.add("名のよみが入力されていません。");
        } else {
            patient.firstNameYomi = firstNameYomi;
        }
        LocalDate birthdayDate = birthdayInput.getValue();
        if (birthdayDate == null) {
            errs.add("生年月日の入力が不適切です。");
        } else {
            patient.birthday = birthdayDate.toString();
        }
        patient.sex = sexInput.getValue().getCode();
        patient.address = addressInput.getText();
        patient.phone = phoneInput.getText();
        return errs;
    }

}
