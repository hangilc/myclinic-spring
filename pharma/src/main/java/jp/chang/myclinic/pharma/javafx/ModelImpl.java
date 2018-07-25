package jp.chang.myclinic.pharma.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.pharma.tracking.model.Patient;
import jp.chang.myclinic.pharma.tracking.model.Visit;

public class ModelImpl implements PatientList.Model {

    private int visitId;
    private StringProperty name;
    private ObjectProperty<WqueueWaitState> waitState;

    private ModelImpl(){

    }

    public static ModelImpl fromModel(Visit visit){
        Patient patient = visit.getPatient();
        ModelImpl impl = new ModelImpl();
        impl.visitId = visit.getVisitId();
        impl.name = new SimpleStringProperty();
        impl.name.bind(Bindings.concat(patient.lastNameProperty(), patient.firstNameProperty(),
                "(", patient.lastNameYomiProperty(), patient.firstNameYomiProperty(), ")"));
        impl.waitState = visit.wqueueStateProperty();
        return impl;
    }

    @Override
    public int getVisitId() {
        return visitId;
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public ObjectProperty<WqueueWaitState> waitStateProperty() {
        return waitState;
    }
}
