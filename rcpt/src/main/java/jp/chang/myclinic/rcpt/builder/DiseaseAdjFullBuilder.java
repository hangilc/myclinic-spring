package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DiseaseAdjFullDTO;

public class DiseaseAdjFullBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseAdjFullBuilder.class);
    private DiseaseAdjFullDTO result = new DiseaseAdjFullDTO();

    DiseaseAdjFullBuilder() {

    }

    public DiseaseAdjFullDTO build(){
        if( result.diseaseAdj == null ){
            result.diseaseAdj = new DiseaseAdjBuilder().build();
        }
        if( result.master == null ){
            result.master = new ShuushokugoMasterBuilder().build();
        }
        return result;
    }

}
