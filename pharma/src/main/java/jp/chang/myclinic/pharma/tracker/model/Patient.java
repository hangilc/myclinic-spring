package jp.chang.myclinic.pharma.tracker.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.PatientDTO;

public class Patient {

    private int patientId;
    private StringProperty lastName;
    private StringProperty firstName;

    public Patient(PatientDTO dto){
        this.patientId = dto.patientId;
        this.lastName = new SimpleStringProperty(dto.lastName);
        this.firstName = new SimpleStringProperty(dto.firstName);
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
}
