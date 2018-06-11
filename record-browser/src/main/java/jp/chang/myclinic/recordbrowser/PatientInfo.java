package jp.chang.myclinic.recordbrowser;

import javafx.scene.control.Label;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PatientInfo extends Form {
    private Label patientIdLabel = new Label();
    private Label nameLabel = new Label();
    private Label yomiLabel = new Label();
    private Label birthdayLabel = new Label();
    private Label sexLabel = new Label();
    private Label addressLabel = new Label();
    private Label phoneLabel = new Label();

    public PatientInfo(PatientDTO patient){
        this();
        setPatient(patient);
    }

    public PatientInfo(){
        nameLabel.setWrapText(true);
        yomiLabel.setWrapText(true);
        addressLabel.setWrapText(true);
        phoneLabel.setWrapText(true);
        add("患者番号：", patientIdLabel);
        add("名前：", nameLabel);
        add("読み：", yomiLabel);
        add("生年月日：", birthdayLabel);
        add("性別：", sexLabel);
        add("住所：", addressLabel);
        add("電話：", phoneLabel);
    }

    public void setPatient(PatientDTO patient){
        patientIdLabel.setText("" + patient.patientId);
        nameLabel.setText(patient.lastName + " " + patient.firstName);
        yomiLabel.setText(patient.lastNameYomi + " " + patient.firstNameYomi);
        birthdayLabel.setText(createBirthdayLabel(patient.birthday));
        sexLabel.setText(Sex.codeToKanji(patient.sex));
        addressLabel.setText(patient.address);
        phoneLabel.setText(patient.phone);
    }

    private String createBirthdayLabel(String sqldate){
        try {
            LocalDate bd = LocalDate.parse(sqldate);
            return String.format("%s (%d才)",
                    DateTimeUtil.toKanji(bd, DateTimeUtil.kanjiFormatter1),
                    DateTimeUtil.calcAge(bd)
                    );
        } catch(DateTimeParseException ex){
            return "(生年月日が不適切です)";
        }
    }

    public void clear(){
        patientIdLabel.setText("");
        nameLabel.setText("");
        yomiLabel.setText("");
        birthdayLabel.setText("");
        sexLabel.setText("");
        addressLabel.setText("");
        phoneLabel.setText("");
    }

}
