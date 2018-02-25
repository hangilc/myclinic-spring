package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDisp extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(PatientDisp.class);

    private int patientId = 0;
    private Text patientIdText = new Text("");
    private Text nameText = new Text("");
    private Text yomiText = new Text("");
    private Text birthdayText = new Text("");
    private Text sexText = new Text("");
    private Text addressText = new Text("");
    private Text phoneText = new Text("");

    public PatientDisp() {
        getStyleClass().add("patient-disp");
        rightAlignFirstColumn();
        addRow("患者番号：", new TextFlow(patientIdText));
        addRow("名前：", new TextFlow(nameText));
        addRow("よみ：", new TextFlow(yomiText));
        addRow("生年月日：", new TextFlow(birthdayText));
        addRow("性別：", new TextFlow(sexText));
        addRow("住所：", new TextFlow(addressText));
        addRow("電話：", new TextFlow(phoneText));
    }

    public int getPatientId(){
        return patientId;
    }

    private void setPatientIdText(String patientIdText) {
        this.patientIdText.setText(patientIdText);
    }

    private void setNameText(String nameText) {
        this.nameText.setText(nameText);
    }

    private void setYomiText(String yomiText) {
        this.yomiText.setText(yomiText);
    }

    private void setBirthdayText(String birthdayText) {
        this.birthdayText.setText(birthdayText);
    }

    private void setSexText(String sexText) {
        this.sexText.setText(sexText);
    }

    private void setAddressText(String addressText) {
        this.addressText.setText(addressText);
    }

    private void setPhoneText(String phoneText) {
        this.phoneText.setText(phoneText);
    }

    public void setPatient(PatientDTO patient){
        this.patientId = patient.patientId;
        setPatientIdText("" + patient.patientId);
        setNameText(patient.lastName + " " + patient.firstName);
        setYomiText(patient.lastNameYomi + " " + patient.firstNameYomi);
        setBirthdayText(birthdayRep(patient.birthday));
        setSexText(sexRep(patient.sex));
        setAddressText(patient.address);
        setPhoneText(patient.phone);
    }

    private String birthdayRep(String sqldate){
        try {
            if( "0000-00-00".equals(sqldate) ){
                return "不明";
            }
            return DateTimeUtil.sqlDateToKanji(sqldate, DateTimeUtil.kanjiFormatter1);
        } catch(Exception ex){
            return "不明";
        }
    }

    private String sexRep(String code){
        Sex sex = Sex.fromCode(code);
        return sex == null ? "不明" : sex.getKanji();
    }

}
