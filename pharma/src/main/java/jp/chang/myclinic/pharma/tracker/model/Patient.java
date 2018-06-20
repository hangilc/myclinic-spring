package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.PatientDTO;

public class Patient {

    private int patientId;
    private StringProperty lastName;
    private StringProperty firstName;
    private StringProperty lastNameYomi;
    private StringProperty firstNameYomi;

    public Patient(PatientDTO dto){
        this.patientId = dto.patientId;
        this.lastName = new SimpleStringProperty(dto.lastName);
        this.firstName = new SimpleStringProperty(dto.firstName);
        this.lastNameYomi = new SimpleStringProperty(dto.lastNameYomi);
        this.firstNameYomi = new SimpleStringProperty(dto.firstNameYomi);
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

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", lastName=" + lastName +
                ", firstName=" + firstName +
                '}';
    }
}
