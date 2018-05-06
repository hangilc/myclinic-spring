package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.DiseaseAdjDTO;

import java.util.function.Consumer;

public class DiseaseAdjBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseAdjBuilder.class);
    private DiseaseAdjDTO result;

    public DiseaseAdjBuilder() {
        result = new DiseaseAdjDTO();
        result.diseaseAdjId = G.genid();
        result.diseaseId = G.genid();
        result.shuushokugocode = G.genid();
    }

    public DiseaseAdjBuilder(Consumer<DiseaseAdjDTO> cb){
        this();
        cb.accept(result);
    }

    public DiseaseAdjDTO build(){
        return result;
    }

}
