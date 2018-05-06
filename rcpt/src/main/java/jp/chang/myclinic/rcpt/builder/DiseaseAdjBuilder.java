package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DiseaseAdjDTO;

public class DiseaseAdjBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseAdjBuilder.class);
    private DiseaseAdjDTO result = new DiseaseAdjDTO();

    DiseaseAdjBuilder() {

    }

    DiseaseAdjDTO build(){
        return result;
    }

}
