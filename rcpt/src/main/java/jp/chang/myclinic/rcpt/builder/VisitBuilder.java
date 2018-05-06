package jp.chang.myclinic.rcpt.builder;

import jp.chang.myclinic.dto.VisitDTO;

import java.util.function.Consumer;

public class VisitBuilder {

    //private static Logger logger = LoggerFactory.getLogger(VisitBuilder.class);
    private VisitDTO result;

    VisitBuilder() {
        result = new VisitDTO();
        result.visitId = G.genid();
        result.patientId = G.genid();
        result.visitedAt = G.today();
        result.shahokokuhoId = 0;
        result.koukikoureiId = 0;
        result.roujinId = 0;
        result.kouhi1Id = 0;
        result.kouhi2Id = 0;
        result.kouhi3Id = 0;
    }

    public VisitDTO build(){
        return result;
    }

    public VisitBuilder modify(Consumer<VisitDTO> cb){
        cb.accept(result);
        return this;
    }

}
