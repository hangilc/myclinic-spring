package jp.chang.myclinic.reception.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.model.PatientModel;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PatientInfo extends Form {
    private ObjectProperty<PatientModel> model = new SimpleObjectProperty<>(new PatientModel());
    private Label patientIdLabel = new Label();
    private Label nameLabel = new Label();
    private Label yomiLabel = new Label();
    private Label birthdayLabel = new Label();
    private Label sexLabel = new Label();
    private Label addressLabel = new Label();
    private Label phoneLabel = new Label();

    public PatientInfo(){
        patientIdLabel.textProperty().bind(new StringBinding(){
            { bind(model); }

            @Override
            protected String computeValue() {
                PatientModel patientModel = model.getValue();
                if( patientModel == null ){
                    return "";
                }
                int patientId = patientModel.getPatientId();
                return patientId == 0 ? "" : "" + patientId;
            }
        });
        nameLabel.setWrapText(true);
        nameLabel.textProperty().bind(new StringBinding(){
            { bind(model); }

            @Override
            protected String computeValue() {
                PatientModel patientModel = model.getValue();
                if( patientModel == null ){
                    return "";
                }
                return patientModel.getLastName() + " " + patientModel.getFirstName();
            }
        });
        yomiLabel.setWrapText(true);
        yomiLabel.textProperty().bind(new StringBinding(){
            { bind(model); }

            @Override
            protected String computeValue() {
                PatientModel patientModel = model.getValue();
                if( patientModel == null ){
                    return "";
                }
                return patientModel.getLastNameYomi() + " " + patientModel.getFirstNameYomi();
            }
        });
        birthdayLabel.textProperty().bind(new StringBinding(){
            { bind(model); }

            @Override
            protected String computeValue() {
                if( model.getValue() == null ){
                    return "";
                }
                LocalDate date = model.getValue().getBirthday();
                if( date == null || date == LocalDate.MAX ){
                    return "";
                } else {
                    return date.toString();
                }
            }
        });
        sexLabel.textProperty().bind(new StringBinding(){
            { bind(model); }

            @Override
            protected String computeValue() {
                if( model.getValue() == null ){
                    return "";
                } else {
                    Sex sex = model.getValue().getSex();
                    if( sex == null ){
                        return "";
                    } else {
                        return sex.getKanji();
                    }
                }
            }
        });
        addressLabel.setWrapText(true);
        addressLabel.textProperty().bind(Bindings.select(model, "address"));
        phoneLabel.setWrapText(true);
        phoneLabel.textProperty().bind(Bindings.select(model, "phone"));
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
        birthdayLabel.setText(birthdayText(patient.birthday));
        sexLabel.setText(sexText(patient.sex));
        addressLabel.setText(patient.address);
        phoneLabel.setText(patient.phone);
    }

    public PatientModel getModel() {
        return model.get();
    }

    public ObjectProperty<PatientModel> modelProperty() {
        return model;
    }

    public void setModel(PatientModel model) {
        this.model.set(model);
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
