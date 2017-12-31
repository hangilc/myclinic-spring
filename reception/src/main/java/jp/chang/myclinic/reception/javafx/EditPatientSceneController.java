package jp.chang.myclinic.reception.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.ArrayList;
import java.util.List;

public class EditPatientSceneController {

    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameYomiInput;
    @FXML
    private TextField firstNameYomiInput;
    @FXML
    private ComboBox<String> birthdayGengouInput;
    @FXML
    private TextField birthdayNenInput;
    @FXML
    private TextField birthdayMonthInput;
    @FXML
    private TextField birthdayDayInput;
    @FXML
    private RadioButton sexMaleRadio;
    @FXML
    private RadioButton sexFemaleRadio;
    @FXML
    private TextField addressInput;
    @FXML
    private TextField phoneInput;

    @FXML
    public void initialize(){
        birthdayGengouInput.getItems().addAll("平成", "昭和", "大正", "明治");
        birthdayGengouInput.getSelectionModel().select("昭和");
    }

    public void onNewShahokokuhoClick(ActionEvent actionEvent) {
        EditShahokokuhoStage stage = new EditShahokokuhoStage();
        stage.show();
    }

    public void onNewKoukikoureiClick(ActionEvent actionEvent) {
        EditKoureiStage stage = new EditKoureiStage();
        stage.show();
    }

    public void onNewKouhiClick(ActionEvent actionEvent) {
        EditKouhiStage stage = new EditKouhiStage();
        stage.show();
    }

    public void onEnterPatientClick(ActionEvent actionEvent) {
        System.out.println(lastNameInput.getText());
    }

    private List<String> save(PatientDTO patient){
        List<String> errs = new ArrayList<>();
        String lastName = lastNameInput.getText();
        if( lastName.isEmpty() ){
            errs.add("性が入力されていません。");
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
            errs.add("性のよみが入力されていません。");
        } else {
            patient.lastNameYomi = lastNameYomi;
        }
        String firstNameYomi = firstNameYomiInput.getText();
        if( firstNameYomi.isEmpty() ){
            errs.add("名のよみが入力されていません。");
        } else {
            patient.firstNameYomi = firstNameYomi;
        }
        return errs;
    }
}
