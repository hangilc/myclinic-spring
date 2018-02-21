package jp.chang.myclinic.practice.javafx.disease.add;

import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuushokugoSearchResult implements DiseaseSearchResultModel {

    private static Logger logger = LoggerFactory.getLogger(ShuushokugoSearchResult.class);

    private ShuushokugoMasterDTO master;

    public ShuushokugoSearchResult(ShuushokugoMasterDTO master) {
        this.master = master;
    }


    @Override
    public String rep() {
        return master.name;
    }

    @Override
    public void applyTo(DiseaseInput diseaseInput) {
        diseaseInput.addShuushokugo(master);
    }
}
