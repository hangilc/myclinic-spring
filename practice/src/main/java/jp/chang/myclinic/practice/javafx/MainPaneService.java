package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MainPaneService {

    PatientDTO getCurrentPatient();
    int getCurrentVisitId();
    int getTempVisitId();
    default int getCurrentOrTempVisitId(){
        int id = getCurrentVisitId();
        if( id > 0 ){
            return id;
        }
        return getTempVisitId();
    }
    void broadcastNewText(TextDTO newText);

}
