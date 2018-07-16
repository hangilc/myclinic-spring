package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.*;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.WqueueFullDTO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class WqueueDTOModel implements WqueueTable.Model {

    private IntegerProperty waitState;
    private IntegerProperty patientId;
    private StringProperty lastName;
    private StringProperty firstName;
    private StringProperty lastNameYomi;
    private StringProperty firstNameYomi;
    private ObjectProperty<Sex> sex;
    private ObjectProperty<LocalDate> birthday;
    private int visitId;

    public WqueueDTOModel(WqueueFullDTO dto) {
        this.waitState = new SimpleIntegerProperty(dto.wqueue.waitState);
        this.patientId = new SimpleIntegerProperty(dto.patient.patientId);
        this.lastName = new SimpleStringProperty(dto.patient.lastName);
        this.firstName = new SimpleStringProperty(dto.patient.firstName);
        this.lastNameYomi = new SimpleStringProperty(dto.patient.lastNameYomi);
        this.firstNameYomi = new SimpleStringProperty(dto.patient.firstNameYomi);
        this.sex = new SimpleObjectProperty<>(Sex.fromCode(dto.patient.sex));
        this.birthday = new SimpleObjectProperty<>(getLocalDate(dto.patient.birthday));
        this.visitId = dto.visit.visitId;
    }

    private LocalDate getLocalDate(String s){
        try {
            return LocalDate.parse(s);
        } catch(DateTimeParseException e){
            return null;
        }
    }

    @Override
    public IntegerProperty waitStateProperty() {
        return waitState;
    }

    @Override
    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    @Override
    public StringProperty lastNameProperty() {
        return lastName;
    }

    @Override
    public StringProperty firstNameProperty() {
        return firstName;
    }

    @Override
    public StringProperty lastNameYomiProperty() {
        return lastNameYomi;
    }

    @Override
    public StringProperty firstNameYomiProperty() {
        return firstNameYomi;
    }

    @Override
    public ObjectProperty<Sex> sexProperty() {
        return sex;
    }

    @Override
    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    @Override
    public int getVisitId() {
        return visitId;
    }
}
