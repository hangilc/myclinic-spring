package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseDTO;

import java.time.LocalDate;
import java.util.function.Consumer;

public class DiseaseBuilder {

    //private static Logger logger = LoggerFactory.getLogger(DiseaseBuilder.class);
    private DiseaseDTO result;

    public DiseaseBuilder() {
        result = new DiseaseDTO();
        result.diseaseId = G.genid();
        result.patientId = G.genid();
        result.shoubyoumeicode = G.genid();
        result.startDate = LocalDate.now().toString();
        result.endDate = "0000-00-00";
        result.endReason = DiseaseEndReason.NotEnded.getCode();
    }

    public DiseaseBuilder(Consumer<DiseaseDTO> cb){
        this();
        cb.accept(result);
    }

    public DiseaseDTO build(){
        return result;
    }

    public DiseaseBuilder modify(Consumer<DiseaseDTO> cb){
        cb.accept(result);
        return this;
    }

    public DiseaseBuilder setShoubyoumeicode(int shoubyoumeicode){
        result.shoubyoumeicode = shoubyoumeicode;
        return this;
    }

}
