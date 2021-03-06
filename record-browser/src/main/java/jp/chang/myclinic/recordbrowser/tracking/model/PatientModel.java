package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.dto.PatientDTO;

public class PatientModel {

    private int patientId;
    private StringProperty lastName;
    private StringProperty firstName;

    public PatientModel(PatientDTO dto){
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
