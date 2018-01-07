package jp.chang.myclinic.reception.model;

import javafx.beans.property.*;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.reception.lib.DateUtil;

import java.time.LocalDate;

public class PatientModel {
    private IntegerProperty patientId = new SimpleIntegerProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastNameYomi = new SimpleStringProperty();
    private StringProperty firstNameYomi = new SimpleStringProperty();
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    private ObjectProperty<Sex> sex = new SimpleObjectProperty<>();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();

    public static PatientModel fromPatient(PatientDTO patient){
        PatientModel model = new PatientModel();
        model.patientId.set(patient.patientId);
        model.lastName.setValue(patient.lastName);
        model.firstName.setValue(patient.firstName);
        model.lastNameYomi.setValue(patient.lastNameYomi);
        model.firstNameYomi.setValue(patient.firstNameYomi);
        model.birthday.setValue(DateUtil.sqlDateToLocalDate(patient.birthday));
        model.sex.setValue(Sex.fromCode(patient.sex));
        model.address.setValue(patient.address);
        model.phone.setValue(patient.phone);
        return model;
    }

    public int getPatientId() {
        return patientId.get();
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
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

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

}
