package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.reception.tracker.model.Wqueue;

import java.time.LocalDate;

class WqueueModel implements WqueueTable.Model {

    private Wqueue wqueue;
    private IntegerProperty patientId;

    WqueueModel(Wqueue wqueue) {
        this.wqueue = wqueue;
        this.patientId = new SimpleIntegerProperty(wqueue.getVisit().getPatient().getPatientId());
    }

    @Override
    public IntegerProperty waitStateProperty() {
        return wqueue.waitStateProperty();
    }

    @Override
    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    @Override
    public StringProperty lastNameProperty() {
        return wqueue.getVisit().getPatient().lastNameProperty();
    }

    @Override
    public StringProperty firstNameProperty() {
        return wqueue.getVisit().getPatient().firstNameProperty();
    }

    @Override
    public StringProperty lastNameYomiProperty() {
        return wqueue.getVisit().getPatient().lastNameYomiProperty();
    }

    @Override
    public StringProperty firstNameYomiProperty() {
        return wqueue.getVisit().getPatient().firstNameYomiProperty();
    }

    @Override
    public ObjectProperty<Sex> sexProperty() {
        return wqueue.getVisit().getPatient().sexProperty();
    }

    @Override
    public ObjectProperty<LocalDate> birthdayProperty() {
        return wqueue.getVisit().getPatient().birthdayProperty();
    }

    @Override
    public int getVisitId() {
        return wqueue.getVisit().getVisitId();
    }
}
