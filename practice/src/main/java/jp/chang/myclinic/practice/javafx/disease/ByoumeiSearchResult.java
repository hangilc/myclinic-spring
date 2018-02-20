package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByoumeiSearchResult implements DiseaseSearchResultModel {

    private static Logger logger = LoggerFactory.getLogger(ByoumeiSearchResult.class);

    private ByoumeiMasterDTO master;

    public ByoumeiSearchResult(ByoumeiMasterDTO master) {
        this.master = master;
    }

    @Override
    public String rep() {
        return master.name;
    }

    @Override
    public void applyTo(DiseaseInput diseaseInput) {
        diseaseInput.setByoumei(master);
    }

}
