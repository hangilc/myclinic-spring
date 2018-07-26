package jp.chang.myclinic.pharma.tracking.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Patient {

    private static Logger logger = LoggerFactory.getLogger(Patient.class);
    private int patientId;
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastNameYomi = new SimpleStringProperty();
    private StringProperty firstNameYomi = new SimpleStringProperty();
    private ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    private ObjectProperty<Sex> sex = new SimpleObjectProperty<>();

    public Patient(PatientDTO dto) {
        this.patientId = dto.patientId;
        doUpdate(dto);
    }

    private void doUpdate(PatientDTO patientDTO) {
        lastName.setValue(patientDTO.lastName);
        firstName.setValue(patientDTO.firstName);
        lastNameYomi.setValue(patientDTO.lastNameYomi);
        firstNameYomi.setValue(patientDTO.firstNameYomi);
        try {
            birthday.setValue(LocalDate.parse(patientDTO.birthday));
        } catch (DateTimeParseException ex) {
            logger.error("Invalid birthday. {}", patientDTO);
        }
        Sex sexValue = Sex.fromCode(patientDTO.sex);
        if (sex != null) {
            sex.setValue(sexValue);
        } else {
            logger.error("Invalid sex. {}", patientDTO);
        }
    }

    public void update(PatientDTO patientDTO){
        doUpdate(patientDTO);
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
