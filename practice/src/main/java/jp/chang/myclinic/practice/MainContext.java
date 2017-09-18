package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.awt.*;

public interface MainContext {

    void startBrowse(PatientDTO patient, Runnable uiCallback);
    void endBrowse();
    void startExam(PatientDTO patient, VisitDTO visit, Runnable uiCallback);
    void suspendExam(Runnable uiCallback);
    void endExam(int charge, Runnable uiCallback);

    int getCurrentPatientId();
    int getCurrentVisitId();
    int getTempVisitId();

    default int getTargetVisitId(){
        int currentVisitId = getCurrentVisitId();
        if( currentVisitId != 0 ){
            return currentVisitId;
        } else {
            return getTempVisitId();
        }
    }

    static MainContext get(Component comp){
        while( comp != null ){
            if( comp instanceof MainContext ){
                return (MainContext) comp;
            }
            comp = comp.getParent();
        }
        throw new RuntimeException("cannot find MainContext");
    }
}
