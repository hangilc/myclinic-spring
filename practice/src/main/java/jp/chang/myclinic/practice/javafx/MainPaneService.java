package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;

public interface MainPaneService {
    void setCurrent(PatientDTO patient, int visitId);

    PatientDTO getCurrentPatient();

    int getCurrentVisitId();

    int getTempVisitId();

    default int getCurrentOrTempVisitId(){
        int id = getCurrentVisitId();
        if( id > 0 ){
            return id;
        } else {
            return getTempVisitId();
        }
    }

    void broadcastNewText(TextDTO newText);
}
