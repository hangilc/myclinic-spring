package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainStageServiceMock implements MainStageService {

    private String title;

    public String getTitle(){
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

}
