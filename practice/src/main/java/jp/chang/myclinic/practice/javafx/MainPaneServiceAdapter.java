package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPaneServiceAdapter implements MainPaneService {

    @Override
    public PatientDTO getCurrentPatient() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getCurrentVisitId() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int getTempVisitId() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void broadcastNewText(TextDTO newText) {
        throw new RuntimeException("not implemented");
    }

}
