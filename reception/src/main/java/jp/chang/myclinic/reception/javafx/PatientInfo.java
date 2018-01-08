package jp.chang.myclinic.reception.javafx;

import javafx.scene.control.Label;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.DateUtil;

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
        birthdayLabel.setText(DateUtil.sqlDateToString(patient.birthday));
        sexLabel.setText(Sex.codeToKanji(patient.sex));
        addressLabel.setText(patient.address);
        phoneLabel.setText(patient.phone);
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
