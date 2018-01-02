package jp.chang.myclinic.reception.javafx;

import javafx.scene.control.Label;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PatientInfo extends Form {
    private Label nameLabel = new Label();
    {
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(300);
    }
    private Label yomiLabel = new Label();
    {
        yomiLabel.setWrapText(true);
        yomiLabel.setMaxWidth(300);
    }
    private Label birthdayLabel = new Label();
    private Label sexLabel = new Label();
    private Label addressLabel = new Label();
    {
        addressLabel.setWrapText(true);
        addressLabel.setMaxWidth(300);
    }
    private Label phoneLabel = new Label();
    {
        phoneLabel.setWrapText(true);
        phoneLabel.setMaxWidth(300);
    }

    public PatientInfo(){
        add("名前：", nameLabel);
        add("読み：", yomiLabel);
        add("生年月日：", birthdayLabel);
        add("性別：", sexLabel);
        add("住所：", addressLabel);
        add("電話：", phoneLabel);
    }

    public void setPatient(PatientDTO patient){
        nameLabel.setText(patient.lastName + " " + patient.firstName);
        yomiLabel.setText(patient.lastNameYomi + " " + patient.firstNameYomi);
        birthdayLabel.setText(birthdayText(patient.birthday));
        sexLabel.setText(sexText(patient.sex));
        addressLabel.setText(patient.address);
        phoneLabel.setText(patient.phone);
    }

    private String birthdayText(String birthdayRep){
        if( birthdayRep == null || birthdayRep.equals("0000-00-00") ){
            return "（未入力）";
        }
        try {
            LocalDate date = DateTimeUtil.parseSqlDate(birthdayRep);
            String text = DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
            int age = DateTimeUtil.calcAge(date);
            return String.format("%s (%d才)", text, age);
        } catch(DateTimeParseException ex){
            return "(invalid date format)";
        }
    }

    private String sexText(String sexRep){
        Sex sex = Sex.fromCode(sexRep);
        if( sex == null ){
            return "(invalid sex code)";
        } else {
            return sex.getKanji();
        }
    }
}
