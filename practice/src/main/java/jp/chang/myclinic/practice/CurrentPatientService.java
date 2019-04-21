package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CurrentPatientService {

    public static class TempVisitIdChange {
        public int prevTempVisitId;
        public int newTempVisitId;
    }

    private PatientDTO currentPatient;
    private int currentVisitId;
    private int tempVisitId;
    private List<BiConsumer<PatientDTO, Integer>> onChangeHandlers = new ArrayList<>();
    private Consumer<TempVisitIdChange> onTempVisitIdChangedHandler = c -> {};

    public PatientDTO getCurrentPatient() {
        return currentPatient;
    }

    public int getCurrentVisitId() {
        return currentVisitId;
    }

    public int getTempVisitId() {
        return tempVisitId;
    }

    public void setTempVisitId(int tempVisitId) {
        if( currentVisitId != 0 && tempVisitId != 0){
            throw new RuntimeException("Cannot set temp visitId because current visit is is not zero.");
        }
        TempVisitIdChange change = new TempVisitIdChange();
        change.prevTempVisitId = this.tempVisitId;
        this.tempVisitId = tempVisitId;
        change.newTempVisitId = tempVisitId;
        onTempVisitIdChangedHandler.accept(change);
    }

    public int getCurrentOrTempVisitId(){
        int id = getCurrentVisitId();
        return id != 0 ? id : getTempVisitId();
    }

    public boolean isCurrentOrTempVisitId(int visitId){
        return getCurrentVisitId() == visitId || getTempVisitId() == visitId;
    }

    public void setOnTempVisitIdChangedHandler(Consumer<TempVisitIdChange> onTempVisitIdChangedHandler) {
        this.onTempVisitIdChangedHandler = onTempVisitIdChangedHandler;
    }

    public void setCurrentPatient(PatientDTO patient, int visitId) {
        this.currentPatient = patient;
        this.currentVisitId = visitId;
        this.tempVisitId = 0;
        onChangeHandlers.forEach(handler -> handler.accept(patient, visitId));
    }

    public void addOnChangeHandler(BiConsumer<PatientDTO, Integer> handler) {
        onChangeHandlers.add(handler);
    }

}
