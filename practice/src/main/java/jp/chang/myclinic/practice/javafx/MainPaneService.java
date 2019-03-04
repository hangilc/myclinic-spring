package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;

public interface MainPaneService {
    void setCurrent(PatientDTO patient, int visitId);

    PatientDTO getCurrentPatient();

    int getCurrentVisitId();

    int getTempVisitId();

    void broadcastNewText(TextDTO newText);
}
