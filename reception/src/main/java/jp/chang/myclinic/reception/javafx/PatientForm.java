package jp.chang.myclinic.reception.javafx;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientForm extends GridPane {

    private TextField lastNameInput = new TextField();
    private TextField firstNameInput = new TextField();
    private TextField lastNameYomiInput = new TextField();
    private TextField firstNameYomiInput = new TextField();
    private DateInput birthdayInput = new DateInput();
    private RadioButton maleButton = new RadioButton("男");
    private RadioButton femaleButton = new RadioButton("女");
    private TextField addressInput = new TextField();
    private TextField phoneInput = new TextField();
    private ColumnConstraints firstColumnConstraints = new ColumnConstraints();
    {
        firstColumnConstraints.setPrefWidth(Control.USE_COMPUTED_SIZE);
        firstColumnConstraints.setMinWidth(Control.USE_PREF_SIZE);
        firstColumnConstraints.setMaxWidth(Control.USE_PREF_SIZE);
        firstColumnConstraints.setHalignment(HPos.RIGHT);
    }

    public PatientForm(){
        getColumnConstraints().addAll(firstColumnConstraints);
        setHgap(4);
        setVgap(4);
        int rowIndex = 0;
        add(new Label("名前"), 0, rowIndex);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            lastNameInput.setPromptText("姓");
            firstNameInput.setPromptText("名");
            hbox.getChildren().addAll(lastNameInput, firstNameInput);
            add(hbox, 1, rowIndex);
        }
        add(new Label("よみ"), 0, ++rowIndex);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            lastNameYomiInput.setPromptText("姓のよみ");
            firstNameYomiInput.setPromptText("名のよみ");
            hbox.getChildren().addAll(lastNameYomiInput, firstNameYomiInput);
            add(hbox, 1, rowIndex);
        }
        add(new Label("生年月日"), 0, ++rowIndex);
        {
            add(birthdayInput, 1, rowIndex);
        }
        add(new Label("性別"), 0, ++rowIndex);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            ToggleGroup group = new ToggleGroup();
            group.getToggles().addAll(maleButton, femaleButton);
            femaleButton.setSelected(true);
            hbox.getChildren().addAll(maleButton, femaleButton);
            add(hbox, 1, rowIndex);
        }
        add(new Label("住所"), 0, ++rowIndex);
        {
            add(addressInput, 1, rowIndex);
        }
        add(new Label("電話"), 0, ++rowIndex);
        {
            phoneInput.setPrefWidth(200);
            phoneInput.setMaxWidth(Control.USE_PREF_SIZE);
            phoneInput.setMinWidth(Control.USE_PREF_SIZE);
            add(phoneInput, 1, rowIndex);
        }
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
