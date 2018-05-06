package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DiseaseAdjFullDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;

import java.util.ArrayList;

public class DiseaseFullBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseFullBuilder.class);
    private DiseaseFullDTO result;

    public DiseaseFullBuilder() {
        this.result = new DiseaseFullDTO();
        result.master = new ByoumeiMasterBuilder().build();
        result.disease = new DiseaseBuilder()
                .setShoubyoumeicode(result.master.shoubyoumeicode)
                .build();
        result.adjList = new ArrayList<>();
    }

    public DiseaseFullDTO build() {
        return result;
    }

    public DiseaseFullBuilder setShoubyoumeicode(int shoubyoumeicode) {
        result.disease.shoubyoumeicode = shoubyoumeicode;
        result.master.shoubyoumeicode = shoubyoumeicode;
        return this;
    }

    public DiseaseFullBuilder addAdj(DiseaseAdjFullDTO adj) {
        result.adjList.add(adj);
        return this;
    }

}
