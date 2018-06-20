package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.javafx.lib.GuiUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Patient {

    private int patientId;
    private StringProperty lastName;
    private StringProperty firstName;
    private StringProperty lastNameYomi;
    private StringProperty firstNameYomi;
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    private ObjectProperty<Sex> sex = new SimpleObjectProperty<>(Sex.Female);

    public Patient(PatientDTO dto){
        this.patientId = dto.patientId;
        this.lastName = new SimpleStringProperty(dto.lastName);
        this.firstName = new SimpleStringProperty(dto.firstName);
        this.lastNameYomi = new SimpleStringProperty(dto.lastNameYomi);
        this.firstNameYomi = new SimpleStringProperty(dto.firstNameYomi);
        try {
            this.birthday.setValue(LocalDate.parse(dto.birthday));
        } catch(DateTimeParseException ex){
            GuiUtil.alertError("生年月日を取得できませんでした。");
        }
        try {
            this.sex.setValue(Sex.fromCode(dto.sex));
        } catch(Exception ex){
            GuiUtil.alertError("性別の値を取得できませんでした。");
        }
    }

    public int getPatientId() {
        return patientId;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastNameYomi() {
        return lastNameYomi.get();
    }

    public StringProperty lastNameYomiProperty() {
        return lastNameYomi;
    }

    public void setLastNameYomi(String lastNameYomi) {
        this.lastNameYomi.set(lastNameYomi);
    }

    public String getFirstNameYomi() {
        return firstNameYomi.get();
    }

    public StringProperty firstNameYomiProperty() {
        return firstNameYomi;
    }

    public void setFirstNameYomi(String firstNameYomi) {
        this.firstNameYomi.set(firstNameYomi);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public Sex getSex() {
        return sex.get();
    }

    public ObjectProperty<Sex> sexProperty() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex.set(sex);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", lastName=" + lastName +
                ", firstName=" + firstName +
                '}';
    }
}
