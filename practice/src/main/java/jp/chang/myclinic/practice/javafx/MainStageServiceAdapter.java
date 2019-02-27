package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainStageServiceAdapter implements MainStageService {

    @Override
    public void updateTitle(PatientDTO patient) {
        throw new RuntimeException("not implementes");
    }

}
