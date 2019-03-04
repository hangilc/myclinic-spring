package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;

public class MainPaneServiceMock implements MainPaneService {

    private PatientDTO currentPatient;
    private int currentVisitId;
    private int tempVisitId;

    public void setCurrent(PatientDTO patient, int visitId){
        this.currentPatient = patient;
        this.currentVisitId = visitId;
        this.tempVisitId = 0;
    }

    @Override
    public PatientDTO getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public int getCurrentVisitId() {
        return currentVisitId;
    }

    @Override
    public int getTempVisitId() {
        return tempVisitId;
    }

    @Override
    public void broadcastNewText(TextDTO newText) {
        throw new RuntimeException("not implemented");
    }

}
