package jp.chang.myclinic.pharma.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.tracking.model.Patient;
import jp.chang.myclinic.pharma.tracking.model.Visit;

public class ModelImpl implements PatientList.Model {

    private int visitId;
    private StringProperty name;
    private ObjectProperty<WqueueWaitState> waitState;

    private ModelImpl(){

    }

    private ModelImpl(String nameValue, WqueueWaitState waitStateValue){
        this.name = new SimpleStringProperty(nameValue);
        this.waitState = new SimpleObjectProperty<>(waitStateValue);
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

    public static ModelImpl fromPharmaQueueFullDTO(PharmaQueueFullDTO dto){
        PatientDTO patient = dto.patient;
        String nameValue = String.format("%s%s(%s%s)", patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi);
        WqueueWaitState waitStateValue = null;
        if( dto.wqueue != null ) {
            waitStateValue = WqueueWaitState.fromCode(dto.wqueue.waitState);
        }
        return new ModelImpl(nameValue, waitStateValue);
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
