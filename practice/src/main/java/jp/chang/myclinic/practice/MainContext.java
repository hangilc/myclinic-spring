package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;

import java.awt.*;

public interface MainContext {

    void startExam(PatientDTO patient, VisitDTO visit, Runnable edtCallback);
    int getCurrentVisitId();
    int getTempVisitId();

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
