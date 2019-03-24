package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentPatientService {

    private PatientDTO currentPatient;
    private int currentVisitId;
    private int tempVisitId;
    private Runnable onChangeHandler = () -> {};

    public PatientDTO getCurrentPatient() {
        return currentPatient;
    }

    public int getCurrentVisitId() {
        return currentVisitId;
    }

    public int getTempVisitId() {
        return tempVisitId;
    }

    public int getCurrentOrTempVisitId(){
        int id = getCurrentVisitId();
        return id != 0 ? id : getTempVisitId();
    }

    public void setCurrentPatient(PatientDTO patient, int visitId) {
        this.currentPatient = patient;
        this.currentVisitId = visitId;
        this.tempVisitId = 0;
        onChangeHandler.run();
    }

    public void onChange(Runnable handler) {
        this.onChangeHandler = handler;
    }

}
